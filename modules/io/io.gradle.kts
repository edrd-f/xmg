kotlin {
	nativeTarget.compilations["main"].cinterops {
		create("glibc") {
			defFile("src/nativeMain/cinterop/glibc.def")
			includeDirs("/usr/include")
		}
	}
	strictExplicitApi()
}
