package io.gitlab.edrd.xmousegrabber.internal

import kotlin.system.exitProcess

fun fail(message: String): Nothing {
	println(message)
	exitProcess(1)
}
