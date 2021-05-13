plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
}

val kotlinVersion = file("../kotlin.version").readText().trim()

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

kotlinDslPluginOptions {
	experimentalWarning.set(false)
}
