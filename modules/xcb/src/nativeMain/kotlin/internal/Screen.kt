package io.gitlab.edrd.logimanager.xcb.internal

import internal.cinterop.xcb.xcb_grab_button
import internal.cinterop.xcb.xcb_screen_t
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.pointed

internal class Screen(
	private val connection: Connection,
	private val reference: CPointer<xcb_screen_t>
) {
	fun grabButton(buttonNumber: Int, propagateEvents: Boolean = false) {
		xcb_grab_button(
			connection.reference,
			owner_events = (if (propagateEvents) 1 else 0).convert(),
			grab_window = reference.pointed.root,
			event_mask = EventMask.ButtonPress,
			pointer_mode = GrabMode.Async,
			keyboard_mode = GrabMode.Async,
			confine_to = reference.pointed.root,
			cursor = XcbNone,
			button = buttonNumber.convert(),
			modifiers = ModifierMask.Any
		)
	}
}
