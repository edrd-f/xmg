plugins {
	id("com.bnorm.power.kotlin-power-assert") version "0.7.0"
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
