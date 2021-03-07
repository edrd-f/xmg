import io.gitlab.edrd.xmousegrabber.config.Configuration
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration
import io.gitlab.edrd.xmousegrabber.testUtil.assertInstanceOf
import kotlin.contracts.ExperimentalContracts
import kotlin.test.Test

@ExperimentalContracts
class ConfigurationTest {
	@Test fun invalidSyntax() {
		val result = Configuration.loadFromToml("*$@value=1")

		assert(result.isLeft)

		result.left.let { left ->
			assertInstanceOf<InvalidConfiguration.ParseError>(left)
			assert(left.message == "line 1: missing =")
		}
	}

	@Test fun missingOrInvalidVersion() {
		val result = Configuration.loadFromToml("")

		assert(result.isLeft)
		assertInstanceOf<InvalidConfiguration.MissingOrInvalidVersion>(result.left)
	}

	@Test fun unsupportedVersion() {
		val result = Configuration.loadFromToml("version = 35")

		assert(result.isLeft)
		result.left.let { left ->
			assertInstanceOf<InvalidConfiguration.UnsupportedVersion>(left)
			assert(left.value == 35L)
		}
	}

	@Test fun missingOrInvalidMappings() {
		val result = Configuration.loadFromToml("version = 1")

		assert(result.isLeft)
		assertInstanceOf<InvalidConfiguration.MissingOrInvalidMappingsType>(result.left)
	}

	@Test fun missingButtonNumber() {
		val result = Configuration.loadFromToml("""
			version = 1
			[[mappings]]
			  command = 'echo 1'
		""".trimIndent())

		assert(result.isLeft)

		result.left.let { left ->
			assertInstanceOf<InvalidConfiguration.MissingOrInvalidButtonNumberType>(left)
			assert(left.location == "mappings[0]")
		}
	}

	@Test fun invalidMappingsType() {
		val result = Configuration.loadFromToml("""
			version = 1
			mappings = { number = 8 }
		""".trimIndent())

		assert(result.isLeft)
		assertInstanceOf<InvalidConfiguration.MissingOrInvalidMappingsType>(result.left)
	}

	@Test fun unexpectedType() {
		val result = Configuration.loadFromToml("""
			version = 1
			mappings = [8]
		""".trimIndent())

		assert(result.isLeft)

		result.left.let { left ->
			assertInstanceOf<InvalidConfiguration.UnexpectedType>(left)
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

	// TODO
	// @Test fun duplicateButtonNumber()
}
