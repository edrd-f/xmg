package io.gitlab.edrd.logimanager.xcb

import io.gitlab.edrd.logimanager.xcb.internal.Connection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

public class Xcb {
	public sealed class Event {
		public class ButtonPress(public val buttonNumber: Int) : Event()
	}

	public val events: Channel<Event> by lazy {
		startEventPollingLoop()
		eventChannel
	}

	public fun grabButton(buttonNumber: Int) {
		connection.screens.forEach { screen ->
			screen.grabButton(buttonNumber, propagateEvents = false)
		}
		connection.flush()
	}

	private fun startEventPollingLoop() = GlobalScope.launch {
		while (true) {
			yield()
			connection.pollForEvent()?.let { eventChannel.send(it) } ?: continue
		}
	}

	private val eventChannel = Channel<Event>(capacity = 128)

	private val connection by lazy { Connection() }
}
