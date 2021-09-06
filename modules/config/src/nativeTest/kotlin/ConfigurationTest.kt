import io.gitlab.edrd.xmousegrabber.config.Configuration
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration
import kotlin.contracts.ExperimentalContracts
import kotlin.test.Test
import kotlin.test.assertIs

@ExperimentalContracts
class ConfigurationTest {
	@Test fun invalidSyntax() {
		val result = Configuration.loadFromToml("*$@value=1")

		assert(result.isLeft)

		result.left.let { left ->
			assertIs<InvalidConfiguration.ParseError>(left)
			assert(left.message == "line 1: missing =")
		}
	}

	@Test fun missingOrInvalidVersion() {
		val result = Configuration.loadFromToml("")

		assert(result.isLeft)
		assertIs<InvalidConfiguration.MissingOrInvalidVersion>(result.left)
	}

	@Test fun unsupportedVersion() {
		val result = Configuration.loadFromToml("version = 35")

		assert(result.isLeft)
		result.left.let { left ->
			assertIs<InvalidConfiguration.UnsupportedVersion>(left)
			assert(left.value == 35L)
		}
	}

	@Test fun missingOrInvalidMappings() {
		val result = Configuration.loadFromToml("version = 1")

		assert(result.isLeft)
		assertIs<InvalidConfiguration.MissingOrInvalidMappingsType>(result.left)
	}

	@Test fun missingButtonNumber() {
		val result = Configuration.loadFromToml("""
			version = 1
			[[mappings]]
			  command = 'echo 1'
		""".trimIndent())

		assert(result.isLeft)

		result.left.let { left ->
			assertIs<InvalidConfiguration.MissingOrInvalidButtonNumberType>(left)
			assert(left.location == "mappings[0]")
		}
	}

	@Test fun invalidMappingsType() {
		val result = Configuration.loadFromToml("""
			version = 1
			mappings = { number = 8 }
		""".trimIndent())

		assert(result.isLeft)
		assertIs<InvalidConfiguration.MissingOrInvalidMappingsType>(result.left)
	}

	@Test fun unexpectedType() {
		val result = Configuration.loadFromToml("""
			version = 1
			mappings = [8]
		""".trimIndent())

		assert(result.isLeft)

		result.left.let { left ->
			assertIs<InvalidConfiguration.UnexpectedType>(left)
			assert(left.location == "mappings[0]")
			assert(left.expected == "table")
		}
	}

	@Test fun validConfig() {
		val result = Configuration.loadFromToml("""
			version = 1

			[[mappings]]
			  button = 8
				command = '''
				  qdbus org.kde.kglobalaccel
					  /component/kwin invokeShortcut "ExposeAll"
				'''

			[[mappings]]
			  button = 9
				command = 'echo 1'
		""".trimIndent())

		assert(result.isRight)

		val expectedConfiguration = Configuration(
			version = 1.0,
			buttons = listOf(
				Configuration.Button(
					number = 8,
					command = "qdbus org.kde.kglobalaccel /component/kwin invokeShortcut \"ExposeAll\""
				),
				Configuration.Button(
					number = 9,
					command = "echo 1"
				)
			)
		)

		assert(result.right == expectedConfiguration)
	}

	 @Test fun duplicateButtonNumber() {
	 	 val result = Configuration.loadFromToml("""
		    version = 1
		 	  [[mappings]]
			    button = 8
				  command = ''
				[[mappings]]
				  button = 8
					command = ''
	   """.trimIndent())

		 assert(result.isLeft)

		 result.left.let { left ->
			 assertIs<InvalidConfiguration.DuplicateButtonNumber>(left)
			 assert(left.number == 8)
		 }
	 }
}
