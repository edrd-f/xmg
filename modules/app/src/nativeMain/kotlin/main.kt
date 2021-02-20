package io.gitlab.edrd.logimanager

fun main(args: Array<String>) {
	if (args.isEmpty()) {
		println("Usage: logi-manager /absolute/path/to/config.properties")
	}
	Application(configFilePath = args.first()).run()
}
