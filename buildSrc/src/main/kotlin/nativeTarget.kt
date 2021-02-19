import org.gradle.api.GradleException
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests

val KotlinMultiplatformExtension.nativeTarget: KotlinNativeTargetWithHostTests get() {
	return cache.getOrPut(this) {
		val hostOs = System.getProperty("os.name")
		val isMingwX64 = hostOs.startsWith("Windows")
		when {
			hostOs == "Mac OS X" -> macosX64("native")
			hostOs == "Linux" -> linuxX64("native")
			isMingwX64 -> mingwX64("native")
			else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
		}
	}
}

private val cache = HashMap<KotlinMultiplatformExtension, KotlinNativeTargetWithHostTests>()
