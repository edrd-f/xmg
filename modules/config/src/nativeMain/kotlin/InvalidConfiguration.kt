package io.gitlab.edrd.xmousegrabber.config

public sealed class InvalidConfiguration {
	public object MissingOrInvalidVersion : InvalidConfiguration()

	public class UnsupportedVersion(public val value: Long) : InvalidConfiguration()

	public object MissingOrInvalidMappingsType : InvalidConfiguration()

	public data class UnexpectedType(
		public val location: String,
		public val expected: String
	) : InvalidConfiguration()

	public data class MissingOrInvalidButtonNumberType(public val location: String) : InvalidConfiguration()

	public data class MissingOrInvalidCommandType(public val location: String) : InvalidConfiguration()

	public data class ParseError(public val message: String) : InvalidConfiguration()
}
