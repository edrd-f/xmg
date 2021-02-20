package io.gitlab.edrd.logimanager.xcb.internal

import internal.cinterop.xcb.uint16_t
import internal.cinterop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_ANY
import kotlinx.cinterop.convert

internal object ModifierMask {
	val Any = XCB_MOD_MASK_ANY.value.convert<uint16_t>()
}
