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

	public fun iterator(): Iterator<PolymorphicValueAccessor> = object : Iterator<PolymorphicValueAccessor> {
		var currentIndex = 0
		val size = toml_array_nelem(reference)

		override fun hasNext(): Boolean = currentIndex < size

		override fun next(): PolymorphicValueAccessor = PolymorphicValueAccessor(currentIndex++)
	}

	public inner class PolymorphicValueAccessor internal constructor(private val index: Int) {
		public fun getString(): String? = getString(index)
		public fun getLong(): Long? = getLong(index)
		public fun getArray(): TomlArray? = getArray(index)
		public fun getTable(): TomlTable? = getTable(index)
	}
}
