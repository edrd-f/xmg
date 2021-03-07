import io.gitlab.edrd.xmousegrabber.toml.TomlParser
import util.successResultOrFail
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TomlTableTest {
	@Test
	fun stringValue() {
		val result = parser.parse("""
			scheme = "https"
			host = 'localhost'
		""".trimIndent())

		val topLevelTable = result.successResultOrFail()

		assertEquals("https", topLevelTable.getString("scheme"))
		assertEquals("localhost", topLevelTable.getString("host"))
	}

	@Test
	fun longValue() {
		val result = parser.parse("""
			version = 5
		""".trimIndent())

		val topLevelTable = result.successResultOrFail()

		assertEquals(5, topLevelTable.getLong("version"))
	}

	@Test fun arrayValue() {
		val result = parser.parse("""
			[data]
			artists = [{ name = "At The Drive-In" }]
		""".trimIndent())

		val topLevelTable = result.successResultOrFail()
		val data = topLevelTable.getTable("data").let(::assertNotNull)
		val artists = data.getArray("artists").let(::assertNotNull)

		assertEquals("At The Drive-In", artists.getTable(0)?.getString("name"))
	}

	@Test fun nestedTableValue() {
		val result = parser.parse("""
			[server]
			address = { port = 8080 }
		""".trimIndent())

		val topLevelTable = result.successResultOrFail()

		assertEquals(8080, topLevelTable.getTable("server")?.getTable("address")?.getLong("port"))
	}

	@Test fun absentValue() {
		val result = parser.parse("zero = 0")

		val topLevelTable = result.successResultOrFail()

		assertNull(topLevelTable.getLong("one"))
	}

	private val parser = TomlParser()
}
