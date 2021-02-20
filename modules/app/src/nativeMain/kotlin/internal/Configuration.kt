package io.gitlab.edrd.logimanager.internal

import com.soywiz.korio.file.std.LocalVfs
import com.soywiz.korio.util.loadProperties

data class Configuration(val buttons: List<Button>) {
	data class Button(val number: Int, val command: String)

	private val buttonMap = buttons.associateBy { it.number }

	fun button(number: Int): Button? = buttonMap[number]

	companion object {
		suspend fun loadFromFile(path: String): Configuration {
			val properties = LocalVfs[path].loadProperties()

			val buttons = properties.map { (key, value) ->
				val (buttonNumber) = configPattern.find(key)?.destructured
					?: invalidConfigError("Invalid configuration key: $key")

				Button(number = buttonNumber.toInt(), command = value)
			}

			return Configuration(buttons)
		}

		private val configPattern = Regex("^\\s*button\\.(\\d+)\\.command")

		private fun invalidConfigError(message: String): Nothing {
			throw IllegalArgumentException(message)
		}
	}
}
