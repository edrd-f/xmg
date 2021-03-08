package io.gitlab.edrd.xmousegrabber

import io.gitlab.edrd.xmousegrabber.common.rightOr
import io.gitlab.edrd.xmousegrabber.config.Configuration
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration
import io.gitlab.edrd.xmousegrabber.internal.fail
import io.gitlab.edrd.xmousegrabber.internal.messageForConfigurationError
import io.gitlab.edrd.xmousegrabber.io.File

fun main(args: Array<String>) {
	if (args.isEmpty()) {
		fail("Usage: xmg <config-file-path>")
	}

	val configFile = File
		.forRelativeOrAbsolutePath(args.first())
		.also(::checkFileExists)

	Application(configuration = loadConfiguration(configFile)).run()
}

fun checkFileExists(file: File) {
	if (!file.exists()) fail("File ${file.path} does not exist.")
}

fun loadConfiguration(configFile: File): Configuration {
	return Configuration
		.loadFromToml(configFile.readText())
		.rightOr(::failWithConfigurationError)
}

fun failWithConfigurationError(error: InvalidConfiguration): Nothing {
	fail("Invalid configuration: ${messageForConfigurationError(error)}.")
}
