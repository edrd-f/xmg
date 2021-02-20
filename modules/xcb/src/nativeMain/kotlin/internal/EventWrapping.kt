package io.gitlab.edrd.logimanager.xcb.internal

import internal.cinterop.xcb.xcb_button_press_event_t
import internal.cinterop.xcb.xcb_generic_event_t
import io.gitlab.edrd.logimanager.xcb.Xcb
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret

internal fun CPointer<xcb_generic_event_t>.toPublicEvent(): Xcb.Event {
	return when (val responseType = this.pointed.response_type.convert<Int>()) {
		ResponseType.ButtonPress -> convertButtonPressEvent(this)
		else -> error("Unsupported event type: $responseType")
	}
}

private fun convertButtonPressEvent(event: CPointer<xcb_generic_event_t>): Xcb.Event.ButtonPress {
	val buttonPressEvent = event.pointed.reinterpret<xcb_button_press_event_t>()

	return Xcb.Event.ButtonPress(
		buttonNumber = buttonPressEvent.detail.convert()
	).also {
		nativeHeap.free(event.rawValue)
	}
}
