package io.gitlab.edrd.logimanager.internal

import cinterop.xcb.XCB_BUTTON_PRESS
import cinterop.xcb.XCB_NONE
import cinterop.xcb.xcb_button_press_event_t
import cinterop.xcb.xcb_connect
import cinterop.xcb.xcb_event_mask_t
import cinterop.xcb.xcb_flush
import cinterop.xcb.xcb_generic_event_t
import cinterop.xcb.xcb_get_setup
import cinterop.xcb.xcb_grab_button
import cinterop.xcb.xcb_grab_mode_t
import cinterop.xcb.xcb_mod_mask_t
import cinterop.xcb.xcb_poll_for_event
import cinterop.xcb.xcb_screen_iterator_t
import cinterop.xcb.xcb_screen_next
import cinterop.xcb.xcb_setup_roots_iterator
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.nativeHeap.free
import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.useContents
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class Xcb(private val logger: Logger) {
	private val eventChannel = Channel<Event>(capacity = 128)

	sealed class Event {
		class ButtonPress(val buttonNumber: Int) : Event()
	}

	val events by lazy {
		startEventPollingLoop()
		eventChannel
	}

	private fun startEventPollingLoop() = GlobalScope.launch {
		while (true) {
			yield()

			val event = xcb_poll_for_event(connection) ?: continue

			when (event.pointed.response_type.convert<Int>()) {
				XCB_BUTTON_PRESS -> handleButtonPressEvent(event)
			}
		}
	}

	private suspend fun handleButtonPressEvent(event: CPointer<xcb_generic_event_t>) {
		logger.debug("Button press event received")

		val buttonPressEvent = event.pointed.reinterpret<xcb_button_press_event_t>()
		val buttonNumber = buttonPressEvent.detail.convert<Int>()

		free(event.rawValue)

		eventChannel.send(Event.ButtonPress(buttonNumber))
	}

	fun grabButton(buttonNumber: Byte): Unit = screens.forEach { screen ->
		xcb_grab_button(
			connection,
			owner_events = +0, // do not propagate events to window
			grab_window = screen.pointed.root,
			event_mask = xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS.value.convert(),
			pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.value.convert(),
			keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.value.convert(),
			confine_to = screen.pointed.root,
			cursor = XCB_NONE.convert(),
			button = buttonNumber.convert(),
			modifiers = xcb_mod_mask_t.XCB_MOD_MASK_ANY.value.convert()
		)
	}.also {
		xcb_flush(connection)
	}

	private val connection by lazy { xcb_connect(null, null) }

	private val screens by lazy {
		val setup = xcb_get_setup(connection)
		val iterator = xcb_setup_roots_iterator(setup)
		iterator.useContents { (0..size).map { data!!.also { xcb_screen_next(iterator) } } }
	}

	private inline val xcb_screen_iterator_t.size get() = rem
}
