package io.gitlab.edrd.xmousegrabber

import io.gitlab.edrd.xmousegrabber.common.rightOr
import io.gitlab.edrd.xmousegrabber.config.Configuration
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration
import io.gitlab.edrd.xmousegrabber.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	if (args.isEmpty()) {
		fail("Usage: xmg <config-file-path>")
	}

	val configFile = File.forRelativeOrAbsolutePath(args.first())

	if (!configFile.exists()) {
		fail("File ${configFile.path} does not exist.")
	}

	val configuration = Configuration
		.loadFromToml(configFile.readText())
		.rightOr(::failWithConfigurationError)

	Application(configuration).run()
}

fun failWithConfigurationError(error: InvalidConfiguration): Nothing = when (error) {
	InvalidConfiguration.MissingOrInvalidVersion -> "missing 'version' key"
	InvalidConfiguration.MissingOrInvalidMappingsType -> "missing or invalid type for 'mappings' key"
	is InvalidConfiguration.UnexpectedType -> "key ${error.location} must be of ${error.expected} type"
	is InvalidConfiguration.MissingOrInvalidButtonNumberType ->
		"missing or invalid type for 'button' key in ${error.location}"
	is InvalidConfiguration.MissingOrInvalidCommandType ->
		"missing or invalid type for 'command' key in ${error.location}"
	is InvalidConfiguration.ParseError -> error.message
	is InvalidConfiguration.UnsupportedVersion -> "version ${error.value} is not supported"
}.let {
	fail("Invalid configuration: $it.")
}

private fun fail(message: String): Nothing {
	println(message)
	exitProcess(1)
}
