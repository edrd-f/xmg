package io.gitlab.edrd.xmousegrabber.config.internal

import io.gitlab.edrd.xmousegrabber.common.*
import io.gitlab.edrd.xmousegrabber.config.Configuration
import io.gitlab.edrd.xmousegrabber.config.ConfigurationEither
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration.*
import io.gitlab.edrd.xmousegrabber.toml.TomlArray
import io.gitlab.edrd.xmousegrabber.toml.TomlRootTable
import io.gitlab.edrd.xmousegrabber.toml.TomlTable

internal class V1ConfigurationLoader
{
	fun load(rootTable: TomlRootTable): ConfigurationEither {
		return rootTable
			.getArray("mappings")
			.rightIfNotNull(left = { MissingOrInvalidMappingsType })
			.flatMap(::getButtonsFromMappings)
			.map { buttons -> Configuration(version = 1.0, buttons) }
	}

	private fun getButtonsFromMappings(mappings: TomlArray) = mappings
		.iterator()
		.asSequence()
		.fold(
			initial = Either.Right(emptyList<Configuration.Button>())
				as Either<InvalidConfiguration, List<Configuration.Button>>
		) { accumulator, mapping ->
			buildButton(mapping).flatMap { button -> accumulator + button }
		}

	private fun buildButton(
		mapping: TomlArray.PolymorphicValueAccessor
	): Either<InvalidConfiguration, Configuration.Button> {
		val location = "mappings[${mapping.index}]"
		return mapping.getTable()
			.rightIfNotNull(left = { UnexpectedType(location, expected = "table") })
			.flatMap { table -> buildButton(table, location) }
	}

	private fun buildButton(
		table: TomlTable,
		location: String
	): Either<InvalidConfiguration, Configuration.Button> {
		return table.getLong("button")?.toInt()
			.rightIfNotNull(left = { MissingOrInvalidButtonNumberType(location) })
			.flatMap { button ->
				getCommand(table, location).map { Configuration.Button(button, it) }
			}
	}

	private fun getCommand(
		table: TomlTable,
		location: String
	): Either<MissingOrInvalidCommandType, String> {
		return table.getString("command")
			?.let(::normalizeCommand)
			.rightIfNotNull(left = { MissingOrInvalidCommandType(location) })
	}

	private fun normalizeCommand(command: String) = command
		.split("\n")
		.filterNot { it.isBlank() }
		.joinToString(separator = " ") { it.trim() }
}
