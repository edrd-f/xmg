repositories {
	// TODO: remove this once Korlibs migrate to Maven Central
	maven("https://dl.bintray.com/korlibs/korlibs")
	jcenter()
}

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
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
			implementation("com.soywiz.korlibs.korio:korio:2.0.8")
		}
	}
}
