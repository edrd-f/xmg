plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
}

dependencies {
	// TODO: move version to pluginManagement (settings.gradle.kts)
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
}

kotlinDslPluginOptions {
	experimentalWarning.set(false)
}
