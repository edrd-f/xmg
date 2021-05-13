plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
}

dependencies {
	// TODO: find a way to extract the version out of here
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
}

kotlinDslPluginOptions {
	experimentalWarning.set(false)
}
