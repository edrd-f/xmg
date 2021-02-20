package io.gitlab.edrd.logimanager.xcb.internal

import internal.cinterop.xcb.uint8_t
import internal.cinterop.xcb.xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC
import kotlinx.cinterop.convert

internal object GrabMode {
	val Async = XCB_GRAB_MODE_ASYNC.value.convert<uint8_t>()
}
