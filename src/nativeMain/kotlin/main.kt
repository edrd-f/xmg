package io.gitlab.edrd.logimanager

import io.gitlab.edrd.logimanager.internal.Configuration
import io.gitlab.edrd.logimanager.internal.Logger
import io.gitlab.edrd.logimanager.internal.Xcb
import kotlinx.coroutines.runBlocking
import platform.posix.system

fun main(args: Array<String>) = runBlocking {
	if (args.isEmpty()) {
		println("Usage: logi-manager config.properties")
	}

	val configuration = Configuration.loadFromFile(args.first())

	val xcb = Xcb(logger)

	setOf<Byte>(8, 9).forEach { xcb.grabButton(it) }

	val events = xcb.events

	logger.info("Listening for events")

	while (true) {
		handleEvent(events.receive(), configuration)
	}
}

private fun handleEvent(event: Xcb.Event, configuration: Configuration) = when (event) {
	is Xcb.Event.ButtonPress -> {
		logger.debug("Received mouse button ${event.buttonNumber} press")

		when (event.buttonNumber) {
			8 -> system(configuration.button8Action)
			9 -> system(configuration.button9Action)
			else -> error("Button ${event.buttonNumber} not supported")
		}
	}
}

private val logger = Logger(Logger.Level.Info)
