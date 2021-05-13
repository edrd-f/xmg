val kotlinCoroutinesVersion: String by project

kotlin {
  nativeTarget.compilations["main"].cinterops {
		create("xcb") {
			defFile("src/nativeMain/cinterop/xcb.def")
			includeDirs("/usr/include")
		}
	}

	sourceSets.getByName("nativeMain") {
		dependencies {
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
		}
	}

	strictExplicitApi()
}
