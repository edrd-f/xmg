package io.gitlab.edrd.logimanager

import com.soywiz.korio.lang.Environment
import io.gitlab.edrd.logimanager.internal.Configuration
import io.gitlab.edrd.logimanager.internal.Logger
import io.gitlab.edrd.logimanager.xcb.Xcb
import kotlinx.coroutines.runBlocking
import platform.posix.system

fun main(args: Array<String>) = runBlocking {
	if (args.isEmpty()) {
		println("Usage: logi-manager config.properties")
	}

	val configuration = Configuration.loadFromFile(args.first())

	val xcb = Xcb()

	// TODO: grab buttons defined in config
	setOf(8, 9).forEach { xcb.grabButton(it) }

	val events = xcb.events

	logger.info { "Listening for events" }

	while (true) {
		handleEvent(events.receive(), configuration)
	}
}

private fun handleEvent(event: Xcb.Event, configuration: Configuration) = when (event) {
	is Xcb.Event.ButtonPress -> {
		logger.debug { "Received mouse button ${event.buttonNumber} press" }

		when (event.buttonNumber) {
			8 -> system(configuration.button8Action)
			9 -> system(configuration.button9Action)
			else -> error("Button ${event.buttonNumber} not supported")
		}
	}
}

private val logger = Logger(
	level = Environment["LogLevel"]?.let { Logger.Level.forValue(it) } ?: Logger.Level.Info
)
