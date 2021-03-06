import io.gitlab.edrd.xmousegrabber.toml.Either
import io.gitlab.edrd.xmousegrabber.toml.ParseException
import io.gitlab.edrd.xmousegrabber.toml.TomlParser
import io.gitlab.edrd.xmousegrabber.toml.TomlTable
import kotlin.contracts.ExperimentalContracts
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalContracts
class TomlParserTest {
	@Test fun validSyntax() {
		val result = parser.parse("version = 3")

		assertIsInstance<Either.Right<TomlTable>>(result)
	}

	@Test fun invalidSyntax() {
		val result = parser.parse("..-/value=1")

		assertIsInstance<Either.Left<ParseException>>(result)
		assertEquals("line 1: syntax error", result.value.message)
	}

	private val parser = TomlParser()
}
