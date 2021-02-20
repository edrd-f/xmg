package io.gitlab.edrd.logimanager.xcb.internal

import internal.cinterop.xcb.uint16_t
import internal.cinterop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS
import kotlinx.cinterop.convert

internal object EventMask {
	val ButtonPress = XCB_EVENT_MASK_BUTTON_PRESS.value.convert<uint16_t>()
}
