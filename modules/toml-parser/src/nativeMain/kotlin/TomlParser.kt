package io.gitlab.edrd.xmousegrabber.toml

import internal.cinterop.tomlc99.toml_parse
import internal.cinterop.tomlc99.toml_table_t
import io.gitlab.edrd.xmousegrabber.common.Either
import kotlinx.cinterop.*

public class TomlParser {
	public fun parse(text: String): Either<ParseException, TomlRootTable> {
		val errorsBuffer = ByteArray(size = 200)

		val tomlTableReference: CPointer<toml_table_t> = errorsBuffer.usePinned { pinnedBuffer ->
			toml_parse(
				text.cstr,
				errbuf = pinnedBuffer.addressOf(0),
				errbufsz = errorsBuffer.size
			)
		} ?: return Either.Left(ParseException(errorsBuffer.toKString()))

		return Either.Right(TomlRootTable(tomlTableReference))
	}
}
