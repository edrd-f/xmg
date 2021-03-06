package io.gitlab.edrd.xmousegrabber.toml.internal

import internal.cinterop.tomlc99.anonymousStruct3
import internal.cinterop.tomlc99.toml_datum_t
import kotlinx.cinterop.CValue
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.toKStringFromUtf8
import kotlinx.cinterop.useContents

internal fun CValue<toml_datum_t>.toKotlinStringOrNull(): String? = useContents {
	if (ok == 0) return null
	union.string!!.toKStringFromUtf8().also {
		nativeHeap.free(union.string!!.rawValue)
	}
}

internal fun CValue<toml_datum_t>.toKotlinLongOrNull(): Long? = useContents {
	this.takeIf { ok != 0 }?.union?.int
}

private val toml_datum_t.union get() = u
private val anonymousStruct3.string get() = s
private val anonymousStruct3.int get() = i
