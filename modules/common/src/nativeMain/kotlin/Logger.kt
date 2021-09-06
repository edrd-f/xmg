package io.gitlab.edrd.xmousegrabber.common

public class Logger(public val level: Level) {
	public enum class Level {
		Debug,
		Info;
		public companion object {
			public fun forValue(value: String): Level {
				return values().find { it.name.equals(value, ignoreCase = true) }
					?: throw IllegalArgumentException("Log level '$value' does not exist")
			}
		}
	}

	public inline fun debug(message: () -> String) {
		if (Level.Debug.ordinal >= level.ordinal) println(message())
	}

	public inline fun info(message: () -> String) {
		if (Level.Info.ordinal >= level.ordinal) println(message())
	}
}
