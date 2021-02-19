package io.gitlab.edrd.logimanager.internal

import com.soywiz.korio.file.std.LocalVfs
import com.soywiz.korio.util.Props
import com.soywiz.korio.util.loadProperties

class Configuration(val button8Action: String, val button9Action: String) {
	companion object {
		suspend fun loadFromFile(path: String): Configuration {
			val properties = LocalVfs[path].loadProperties()

			return Configuration(
				button8Action = properties.require("button.8.action"),
				button9Action = properties.require("button.9.action")
			)
		}

		private fun Props.require(name: String): String {
			return this[name] ?: error("Configuration file must have a $name property")
		}
	}
}
