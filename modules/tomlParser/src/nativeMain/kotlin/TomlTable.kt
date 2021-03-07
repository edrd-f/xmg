package io.gitlab.edrd.xmousegrabber.toml

import internal.cinterop.tomlc99.*
import io.gitlab.edrd.xmousegrabber.toml.internal.toKotlinLongOrNull
import io.gitlab.edrd.xmousegrabber.toml.internal.toKotlinStringOrNull
import kotlinx.cinterop.CPointer

public sealed class TomlTable(
	protected val reference: CPointer<toml_table_t>
) : TomlValueContainer<String> {
	override fun getLong(key: String): Long? {
		return toml_int_in(reference, key).toKotlinLongOrNull()
	}

	override fun getString(key: String): String? {
		return toml_string_in(reference, key).toKotlinStringOrNull()
	}

	override fun getArray(key: String): TomlArray? {
		return toml_array_in(reference, key)?.let(::TomlArray)
	}

	override fun getTable(key: String): TomlTable? {
		return toml_table_in(reference, key)?.let(::TomlChildTable)
	}
}

public class TomlChildTable internal constructor(
	reference: CPointer<toml_table_t>
) : TomlTable(reference)

public class TomlRootTable internal constructor(
	reference: CPointer<toml_table_t>
) : TomlTable(reference) {
	public fun free(): Unit = toml_free(reference)
}
