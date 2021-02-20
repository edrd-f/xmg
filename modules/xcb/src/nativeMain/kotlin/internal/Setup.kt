package io.gitlab.edrd.logimanager.xcb.internal

import internal.cinterop.xcb.xcb_get_setup
import internal.cinterop.xcb.xcb_screen_next
import internal.cinterop.xcb.xcb_setup_roots_iterator
import kotlinx.cinterop.useContents

internal class Setup(private val connection: Connection) {
	private val reference = xcb_get_setup(connection.reference)

	fun getRootsIterator(): Iterator<Screen> = object : Iterator<Screen> {
		private val iterator = xcb_setup_roots_iterator(reference)
		private var current = 0

		override fun hasNext() = current < iterator.useContents { rem }

		override fun next(): Screen {
			val screen = Screen(connection, iterator.useContents { data!! })

			xcb_screen_next(iterator)
			current++

			return screen
		}
	}
}
