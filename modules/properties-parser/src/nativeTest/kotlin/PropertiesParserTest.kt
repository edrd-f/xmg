
import io.gitlab.edrd.xmousegrabber.properties.Properties
import kotlin.test.Test
import kotlin.test.assertEquals

class PropertiesParserTest {
	@Test fun simpleProperties() {
		val propertiesText = """
			# This should be ignored
			name = Marvin
			species = robot
		""".trimIndent()

		val expectedEntries = mapOf(
			"name" to "Marvin",
			"species" to "robot"
		)

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun dottedProperties() {
		val propertiesText = """
			character.name = Marvin
			# This should also be ignored
			character.species = robot
		""".trimIndent()

		val expectedEntries = mapOf(
			"character.name" to "Marvin",
			"character.species" to "robot"
		)

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun assignTokenTwice() {
		val propertiesText = """
			abc = def = ghi
			# This should also be ignored
			other = value
		""".trimIndent()

		val expectedEntries = mapOf(
			"abc" to "def = ghi",
			"other" to "value"
		)

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun paddedCommentLine() {
		val propertiesText = """
			# This is a comment
			some.key = some.value
		"""

		val expectedEntries = mapOf("some.key" to "some.value")

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun emptyValue() {
		val propertiesText = """
			some.key =
			some.other
		""".trimIndent()

		val expectedEntries = mapOf(
			"some.key" to "",
			"some.other" to ""
		)

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun multiLineValues() {
		val propertiesText = """
			name = Marvin \
			  the \
				robot
			# This should also be ignored
			other = multi \
			  line   \
				value
			last = a\
			b\
			    c
		""".trimIndent()

		val expectedEntries = mapOf(
			"name" to "Marvin the robot",
			"other" to "multi line   value",
			"last" to "abc"
		)

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun escapedTrailingBackslash() {
		val propertiesText = """
			name = Marvin\\
			species = Robot\\Species
		""".trimIndent()

		val expectedEntries = mapOf(
			"name" to "Marvin\\\\",
			"species" to "Robot\\\\Species"
		)

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun blankLines() {
		val propertiesText = """
			name = Marvin\\

			afterBlankLine = true
			# Comment
			afterComment = true
		""".trimIndent()

		val expectedEntries = mapOf(
			"name" to "Marvin\\\\",
			"afterBlankLine" to "true",
			"afterComment" to "true"
		)

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}

	@Test fun blankLineAfterContinuation() {
		val propertiesText = """
			name = Marvin \
			  the \

			# end
		""".trimIndent()

		val expectedEntries = mapOf("name" to "Marvin the")

		assertEquals(expectedEntries, Properties.parse(propertiesText))
	}
}
