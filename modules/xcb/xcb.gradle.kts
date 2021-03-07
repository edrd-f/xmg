kotlin {
  nativeTarget.compilations["main"].cinterops {
		create("xcb") {
			defFile("src/nativeMain/cinterop/xcb.def")
			includeDirs("/usr/include")
		}
	}

	sourceSets.getByName("nativeMain") {
		dependencies {
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
		}
	}

	strictExplicitApi()
}
