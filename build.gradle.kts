plugins {
  kotlin("multiplatform") version "1.4.30"
}

group = "io.gitlab.edrd"
version = "dev-1"

repositories {
	maven("https://dl.bintray.com/korlibs/korlibs")
	jcenter()
  mavenCentral()
}

kotlin {
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosX64("native")
    hostOs == "Linux" -> linuxX64("native")
    isMingwX64 -> mingwX64("native")
    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }

  nativeTarget.apply {
    binaries {
      executable {
        entryPoint = "io.gitlab.edrd.logimanager.main"
      }
    }
    compilations["main"].cinterops {
      create("xcb") {
        defFile("src/nativeMain/cinterop/xcb.def")
        includeDirs("/usr/include")
      }
    }
  }

	sourceSets.getByName("commonMain") {
		dependencies {
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
			implementation("com.soywiz.korlibs.korio:korio:2.0.8")
		}
	}
}
