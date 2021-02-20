plugins {
  kotlin("multiplatform")
}

allprojects {
	group = "io.gitlab.edrd"
	version = "dev-3"

	apply(plugin = "kotlin-multiplatform")

	repositories {
		mavenCentral()
	}

	// Initialize native target
	kotlin { nativeTarget }
}

tasks.create("release") {
	dependsOn(tasks.getByPath(":app:linkReleaseExecutableNative"))

	doLast {
		val appOutputDir = "${project(":app").buildDir}/bin/native/releaseExecutable"
		val binaryOutputFile = file("${project.buildDir}/logi-manager")
		file("$appOutputDir/app.kexe").copyTo(binaryOutputFile, overwrite = true)
		binaryOutputFile.setExecutable(true)
	}
}
