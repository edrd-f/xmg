package io.gitlab.edrd.xmousegrabber.xcb.internal

import internal.cinterop.xcb.XCB_NONE
import internal.cinterop.xcb.xcb_cursor_t
import kotlinx.cinterop.convert

internal val XcbNone = XCB_NONE.convert<xcb_cursor_t>()
