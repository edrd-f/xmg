plugins {
	id("com.bnorm.power.kotlin-power-assert") version "0.8.1"
}

kotlin {
	sourceSets.getByName("nativeMain") {
		dependencies {
			implementation(project(":common"))
			implementation(project(":tomlParser"))
		}
	}

	sourceSets.getByName("nativeTest") {
		dependencies {
			implementation(project(":testUtil"))
		}
	}

	strictExplicitApi()
}
