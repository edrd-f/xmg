plugins {
  kotlin("multiplatform")
}

allprojects {
	group = "io.gitlab.edrd"
	version = "dev-2"

	apply(plugin = "kotlin-multiplatform")

	repositories {
		mavenCentral()
	}

	// Initialize native target
	kotlin { nativeTarget }
}
