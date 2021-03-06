package io.gitlab.edrd.xmousegrabber.toml

import internal.cinterop.tomlc99.*
import io.gitlab.edrd.xmousegrabber.toml.internal.toKotlinLongOrNull
import io.gitlab.edrd.xmousegrabber.toml.internal.toKotlinStringOrNull
import kotlinx.cinterop.CPointer

public class TomlArray internal constructor(
	private val reference: CPointer<toml_array_t>
) : TomlValueContainer<Int> {
	override fun getString(key: Int): String? {
		return toml_string_at(reference, key).toKotlinStringOrNull()
	}

	override fun getLong(key: Int): Long? {
		return toml_int_at(reference, key).toKotlinLongOrNull()
	}

	override fun getArray(key: Int): TomlArray? {
		return toml_array_at(reference, key)?.let(::TomlArray)
	}

	override fun getTable(key: Int): TomlTable? {
		return toml_table_at(reference, key)?.let(::TomlChildTable)
	}
}
