package io.gitlab.edrd.xmousegrabber

import io.gitlab.edrd.xmousegrabber.internal.fileExists
import io.gitlab.edrd.xmousegrabber.internal.filePathForArgument
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	if (args.isEmpty()) {
		fail("Usage: xmg config.properties")
	}

	val configFilePath = filePathForArgument(args.first())

	if (!fileExists(configFilePath)) {
		fail("File $configFilePath does not exist")
	}

	Application(configFilePath).run()
}

private fun fail(message: String) {
	println(message)
	exitProcess(1)
}
