rootProject.name = "x-mouse-grabber"

val modulesDir = file("modules")

requireNotNull(modulesDir.listFiles()) { "Unable to list files under ./modules directory" }
	.asSequence()
	.filter(File::isDirectory)
	.map(File::getName)
	.forEach { dir ->
		val moduleName = dir.replace('.', ':')
		include(":$moduleName")
		project(":$moduleName").apply {
			projectDir = File("${modulesDir.name}/$dir")
			buildFileName = "$dir.gradle.kts"
		}
	}
