package io.gitlab.edrd.xmousegrabber.toml

public interface TomlValueContainer<K> {
	public fun getLong(key: K): Long?

	public fun getString(key: K): String?

	public fun getArray(key: K): TomlArray?

	public fun getTable(key: K): TomlTable?
}
