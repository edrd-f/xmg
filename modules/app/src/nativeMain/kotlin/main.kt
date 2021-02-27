package io.gitlab.edrd.xmousegrabber

import io.gitlab.edrd.xmousegrabber.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	if (args.isEmpty()) {
		fail("Usage: xmg <config-file-path>")
	}

	val configFile = File.forRelativeOrAbsolutePath(args.first())

	if (!configFile.exists()) {
		fail("File ${configFile.path} does not exist")
	}

	Application(configFile).run()
}

private fun fail(message: String) {
	println(message)
	exitProcess(1)
}
