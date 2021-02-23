package io.gitlab.edrd.xmousegrabber

import io.gitlab.edrd.xmousegrabber.internal.filePathForArgument
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	if (args.isEmpty()) {
		println("Usage: xmg config.properties")
		exitProcess(status = 1)
	}

	val configFilePath = filePathForArgument(args.first())

	Application(configFilePath).run()
}
