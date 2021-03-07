package io.gitlab.edrd.xmousegrabber.internal

import io.gitlab.edrd.xmousegrabber.exception.BadInputException
import io.gitlab.edrd.xmousegrabber.io.File
import io.gitlab.edrd.xmousegrabber.io.extension
import io.gitlab.edrd.xmousegrabber.properties.Properties
import io.gitlab.edrd.xmousegrabber.toml.TomlParser
import io.gitlab.edrd.xmousegrabber.toml.rightOrThrow

data class Configuration(val buttons: List<Button>) {
	data class Button(val number: Int, val command: String)

	companion object {
		fun loadFromFile(file: File): Configuration = when (val extension = file.extension) {
			"properties", "props" -> loadFromProperties(file)
			"toml" -> loadFromToml(file)
			else -> throw BadInputException("Configuration file extension '$extension' not supported")
 		}

		// TODO: refactor
		private fun loadFromToml(file: File): Configuration {
			val parser = TomlParser()
			val topLevelTable = file.readText().let(parser::parse).rightOrThrow()
			val mappings = topLevelTable.getArray("mappings") ?: throw BadInputException("Missing 'mappings' configuration")
			val buttons = mappings.iterator().asSequence().map { mapping ->
				val table = mapping.getTable() ?: throw BadInputException("Item within 'mappings' array must be a table")
				Button(
					number = table.getLong("button")?.toInt() ?: throw BadInputException("Button number is required"),
					command = table.getString("command")?.replace("\n", " ") ?: throw BadInputException("Command is required")
				)
			}.toList()
			return Configuration(buttons)
		}

		// TODO: remove after fully migrating to TOML
		private fun loadFromProperties(file: File): Configuration {
			val properties = file.readText().let(Properties::parse)

			val buttons = properties.map { (key, value) ->
				val (buttonNumber) = configPattern.find(key)?.destructured
					?: invalidConfigError(key)

				Button(number = buttonNumber.toInt(), command = value)
			}

			return Configuration(buttons)
		}

		private val configPattern = Regex("^\\s*button\\.(\\d+)\\.command")

		private fun invalidConfigError(key: String): Nothing {
			throw BadInputException("Invalid configuration key: $key")
		}
	}
}
