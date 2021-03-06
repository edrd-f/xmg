package io.gitlab.edrd.xmousegrabber.toml

public sealed class ParseResult {
	public data class Success(public val values: Map<String, Any?>) : ParseResult()

	public data class Error(public val message: String) : ParseResult()

	public fun getOrThrow(): Success = when (this) {
		is Success -> this
		is Error -> throw ParseException(this.message)
	}
}
