package io.gitlab.edrd.logimanager.internal

class Logger(private val level: Level) {
	enum class Level {
		Debug,
		Info
	}

	fun debug(message: String) {
		if (Level.Debug.ordinal >= level.ordinal) println(message)
	}

	fun info(message: String) {
		if (Level.Info.ordinal >= level.ordinal) println(message)
	}
}
