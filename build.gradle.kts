plugins {
  kotlin("multiplatform")
}

allprojects {
	group = "io.gitlab.edrd"
	version = "dev-4"

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
		val binaryOutputFile = file("${rootProject.buildDir}/xmg")
		file("$appOutputDir/app.kexe").copyTo(binaryOutputFile, overwrite = true)
		binaryOutputFile.setExecutable(true)
	}
}
