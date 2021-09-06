plugins {
	id("com.bnorm.power.kotlin-power-assert") version "0.10.0"
}

kotlin {
	sourceSets.getByName("nativeMain") {
		dependencies {
			implementation(project(":common"))
			implementation(project(":tomlParser"))
		}
	}

	strictExplicitApi()
}
