package io.gitlab.edrd.xmousegrabber.config

import io.gitlab.edrd.xmousegrabber.common.*
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration.*
import io.gitlab.edrd.xmousegrabber.toml.TomlArray
import io.gitlab.edrd.xmousegrabber.toml.TomlParser

public data class Configuration(val version: Double, val buttons: List<Button>) {
	public data class Button(val number: Int, val command: String)

	public companion object {
		public fun loadFromToml(tomlString: String): Either<InvalidConfiguration, Configuration> {
			val parser = TomlParser()

			val topLevelTable = parser.parse(tomlString).rightOr { left ->
				return ParseError(left.message ?: "Unknown parse error").left()
			}

			val version = topLevelTable.getLong("version") ?: return MissingOrInvalidVersion.left()

			if (version != 1L) return UnsupportedVersion(version).left()

			val mappings = topLevelTable.getArray("mappings") ?: return MissingOrInvalidMappingsType.left()

			val buttons = tryGettingButtonsFromMappings(mappings)
				.rightOr { left -> return left.left() }
				.toList()

			return Configuration(version = version.toDouble(), buttons = buttons).right()
		}

		private fun tryGettingButtonsFromMappings(mappings: TomlArray) = mappings.iterator()
			.asSequence()
			.foldIndexed(
				initial = Either.Right(emptyList<Button>()) as Either<InvalidConfiguration, List<Button>>
			) { index, accumulator, mapping ->
				if (accumulator.isLeft) return@foldIndexed accumulator

				tryAccumulatingButton(index, buttons = accumulator.right, mapping)
			}

		private fun tryAccumulatingButton(
			index: Int,
			buttons: List<Button>,
			mapping: TomlArray.PolymorphicValueAccessor
		): Either<InvalidConfiguration, List<Button>> {
			val location = "mappings[$index]"

			val table = mapping.getTable()
				?: return UnexpectedType(location = location, expected = "table").left()

			val button = Button(
				number = table.getLong("button")?.toInt()
					?: return MissingOrInvalidButtonNumberType(location).left(),
				command = table.getString("command")?.let(::normalizeCommand)
					?: return MissingOrInvalidCommandType(location).left()
			)

			return (buttons + button).right()
		}

		private fun normalizeCommand(command: String): String {
			return command.split("\n")
				.filterNot { it.isBlank() }
				.joinToString(separator = " ") { it.trim() }
		}
	}
}
