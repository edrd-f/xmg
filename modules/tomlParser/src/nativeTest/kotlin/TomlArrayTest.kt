import io.gitlab.edrd.xmousegrabber.toml.TomlParser
import util.successResultOrFail
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TomlArrayTest {
	@Test fun absentValue() {
		val result = parser.parse("items = [0]")

		val topLevelTable = result.successResultOrFail()
		val items = topLevelTable.getArray("items").let(::assertNotNull)

		assertNull(items.getLong(1))
	}

	@Test
	fun stringArray() {
		val result = parser.parse("""
			schemes = ["http", "ftp"]
		""".trimIndent())

		val topLevelTable = result.successResultOrFail()
		val items = topLevelTable.getArray("schemes").let(::assertNotNull)

		assertEquals("http", items.getString(0))
		assertEquals("ftp", items.getString(1))
	}

	@Test
	fun tableArray() {
		val result = parser.parse("""
			artists = [{ name = "Milton Nascimento" }, { name = "Fugazi" }]
		""".trimIndent())

		val topLevelTable = result.successResultOrFail()
		val items = topLevelTable.getArray("artists").let(::assertNotNull)

		assertEquals("Milton Nascimento", items.getTable(0)?.getString("name"))
		assertEquals("Fugazi", items.getTable(1)?.getString("name"))
	}

	@Test
	fun longArray() {
		val result = parser.parse("ports = [8080, 8081]")

		val topLevelTable = result.successResultOrFail()
		val items = topLevelTable.getArray("ports").let(::assertNotNull)

		assertEquals(8080, items.getLong(0))
		assertEquals(8081, items.getLong(1))
	}

	@Test
	fun arrayOfArrays() {
		val result = parser.parse("items = [[1], [2]]")

		val topLevelTable = result.successResultOrFail()
		val items = topLevelTable.getArray("items").let(::assertNotNull)

		assertEquals(1, items.getArray(0)?.getLong(0))
		assertEquals(2, items.getArray(1)?.getLong(0))
	}

	private val parser = TomlParser()
}
