package io.gitlab.edrd.xmousegrabber.internal

import io.gitlab.edrd.xmousegrabber.exception.BadInputException
import io.gitlab.edrd.xmousegrabber.io.File
import io.gitlab.edrd.xmousegrabber.properties.Properties

data class Configuration(val buttons: List<Button>) {
	data class Button(val number: Int, val command: String)

	companion object {
		fun loadFromFile(file: File): Configuration {
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
