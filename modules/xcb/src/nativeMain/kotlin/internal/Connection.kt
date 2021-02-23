package io.gitlab.edrd.xmousegrabber.xcb.internal

import internal.cinterop.xcb.xcb_connect
import internal.cinterop.xcb.xcb_flush
import internal.cinterop.xcb.xcb_poll_for_event
import io.gitlab.edrd.xmousegrabber.xcb.Xcb

internal class Connection {
	val reference = xcb_connect(null, null)

	fun flush() {
		xcb_flush(reference)
	}

	fun pollForEvent(): Xcb.Event? {
		return xcb_poll_for_event(reference)?.toPublicEvent()
	}

	val screens: List<Screen> by lazy {
		setup.getRootsIterator().asSequence().toList()
	}

	private val setup by lazy { Setup(connection = this) }
}
