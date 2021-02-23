package io.gitlab.edrd.xmousegrabber

fun main(args: Array<String>) {
	if (args.isEmpty()) {
		println("Usage: x-mouse-grabber /absolute/path/to/config.properties")
	}
	Application(configFilePath = args.first()).run()
}
