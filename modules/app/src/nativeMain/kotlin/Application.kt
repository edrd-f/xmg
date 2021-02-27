package io.gitlab.edrd.xmousegrabber

import com.soywiz.korio.lang.Environment
import io.gitlab.edrd.xmousegrabber.internal.Configuration
import io.gitlab.edrd.xmousegrabber.internal.Logger
import io.gitlab.edrd.xmousegrabber.io.File
import io.gitlab.edrd.xmousegrabber.xcb.Xcb
import kotlinx.coroutines.runBlocking
import platform.posix.system

class Application(private val configFile: File) {
	fun run() = runBlocking {
		configuration.buttons.forEach { xcb.grabButton(it.number) }

		val eventChannel = xcb.events

		logger.info { "Listening for events" }

		while (true) {
			handleEvent(eventChannel.receive())
		}
	}

	private fun handleEvent(event: Xcb.Event) = when (event) {
		is Xcb.Event.ButtonPress -> {
			logger.debug { "Received mouse button ${event.buttonNumber} press" }

			commands[event.buttonNumber]?.let(::system)
				?: logger.info { "No configuration for button ${event.buttonNumber}" }
		}
	}

	private val xcb = Xcb()

	private val configuration = runBlocking {
		Configuration.loadFromFile(configFile)
	}

	private val commands = configuration.buttons.associate { it.number to it.command }

	private val logger = Logger(
		level = Environment["LogLevel"]?.let { Logger.Level.forValue(it) } ?: Logger.Level.Info
	)
}
