val kotlinCoroutinesVersion: String by project

kotlin {
	nativeTarget.apply {
		binaries.executable {
			entryPoint = "io.gitlab.edrd.xmousegrabber.main"
		}
	}

	sourceSets.getByName("nativeMain") {
		dependencies {
			implementation(project(":config"))
			implementation(project(":io"))
			implementation(project(":xcb"))
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
		}
	}
}
