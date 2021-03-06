package io.gitlab.edrd.xmousegrabber.toml

import internal.cinterop.tomlc99.toml_int_in
import internal.cinterop.tomlc99.toml_parse
import internal.cinterop.tomlc99.toml_table_t
import kotlinx.cinterop.*

public class TomlParser {
	public fun parse(text: String): ParseResult {
		val errorsBuffer = ByteArray(size = 200)

		val tomlTable: CPointer<toml_table_t> = errorsBuffer.usePinned { pinnedBuffer ->
			toml_parse(
				text.cstr,
				errbuf = pinnedBuffer.addressOf(0),
				errbufsz = errorsBuffer.size
			)
		} ?: return ParseResult.Error(errorsBuffer.toString())

		val version: Long = toml_int_in(tomlTable, "version").useContents {
			if (this.ok == 0) return ParseResult.Error("Cannot read 'version' field")
			this.u.i
		}

		// TODO: change this
		return ParseResult.Success(mapOf("version" to version))
	}
}
