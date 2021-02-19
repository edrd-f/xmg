package io.gitlab.edrd.logimanager.internal

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
import xcb.XCB_BUTTON_PRESS
import xcb.XCB_NONE
import xcb.xcb_button_press_event_t
import xcb.xcb_connect
import xcb.xcb_event_mask_t
import xcb.xcb_flush
import xcb.xcb_generic_event_t
import xcb.xcb_get_setup
import xcb.xcb_grab_button
import xcb.xcb_grab_mode_t
import xcb.xcb_mod_mask_t
import xcb.xcb_poll_for_event
import xcb.xcb_screen_iterator_t
import xcb.xcb_screen_next
import xcb.xcb_setup_roots_iterator

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
