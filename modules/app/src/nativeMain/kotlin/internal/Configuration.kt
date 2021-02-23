package io.gitlab.edrd.xmousegrabber.internal

import com.soywiz.korio.file.std.LocalVfs
import com.soywiz.korio.util.loadProperties
import io.gitlab.edrd.xmousegrabber.exception.BadInputException

data class Configuration(val buttons: List<Button>) {
	data class Button(val number: Int, val command: String)

	companion object {
		suspend fun loadFromFile(path: String): Configuration {
			val properties = LocalVfs[path].loadProperties()

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
