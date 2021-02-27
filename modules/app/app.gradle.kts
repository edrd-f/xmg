kotlin {
	nativeTarget.apply {
		binaries.executable {
			entryPoint = "io.gitlab.edrd.xmousegrabber.main"
		}
	}

	sourceSets.getByName("commonMain") {
		dependencies {
			implementation(project(":xcb"))
			implementation(project(":io"))
			implementation(project(":properties-parser"))
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
		}
	}
}
