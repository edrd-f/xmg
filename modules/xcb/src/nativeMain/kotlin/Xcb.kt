package io.gitlab.edrd.xmousegrabber.xcb

import io.gitlab.edrd.xmousegrabber.xcb.internal.Connection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

public class Xcb(private val pollIntervalMillis: Long = 5) {
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

	private fun startEventPollingLoop() = coroutineScope.launch {
		while (true) {
			delay(pollIntervalMillis)
			connection.pollForEvent()?.let { eventChannel.send(it) } ?: continue
		}
	}

	private val eventChannel = Channel<Event>(capacity = 128)

	private val connection by lazy { Connection() }

	private val coroutineScope = CoroutineScope(Dispatchers.Default)
}
