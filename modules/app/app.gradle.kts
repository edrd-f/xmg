kotlin {
	nativeTarget.apply {
		binaries.executable {
			entryPoint = "io.gitlab.edrd.xmousegrabber.main"
		}
	}

	sourceSets.getByName("nativeMain") {
		dependencies {
			implementation(project(":xcb"))
			implementation(project(":io"))
			implementation(project(":properties-parser"))
			implementation(project(":toml-parser"))
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
		}
	}
}
