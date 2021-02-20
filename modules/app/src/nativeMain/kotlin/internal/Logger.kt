package io.gitlab.edrd.logimanager.internal

class Logger(@PublishedApi internal val level: Level) {
	enum class Level {
		Debug,
		Info;
		companion object {
			fun forValue(value: String): Level {
				return values().find { it.name.equals(value, ignoreCase = true) }
					?: throw IllegalArgumentException("Log level '$value' does not exist")
			}
		}
	}

	inline fun debug(message: () -> String) {
		if (Level.Debug.ordinal >= level.ordinal) println(message())
	}

	inline fun info(message: () -> String) {
		if (Level.Info.ordinal >= level.ordinal) println(message())
	}
}
