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

val binaryApplicationName = "xmg"

tasks.create("release") {
	dependsOn(tasks.getByPath(":app:linkReleaseExecutableNative"))

	doLast {
		val appOutputDir = "${project(":app").buildDir}/bin/native/releaseExecutable"
		val binaryOutputFile = file("${rootProject.buildDir}/$binaryApplicationName")
		file("$appOutputDir/app.kexe").copyTo(binaryOutputFile, overwrite = true)
		binaryOutputFile.setExecutable(true)
	}
}
