package io.gitlab.edrd.xmousegrabber.io

import internal.cinterop.glibc.get_current_dir_name
import kotlinx.cinterop.toKStringFromUtf8
import platform.posix.*

public class File(public val path: String) {
	public fun readText(): String = buildString {
		val pointer = openOrThrow()
		while (true) {
			// Probably not that efficient, but config files are small so it's ok
			when (val char = fgetc(pointer)) {
				EOF -> break
				else -> append(char.toChar())
			}
		}
		fclose(pointer)
	}

	public fun exists(): Boolean {
		return access(path, F_OK) == 0
	}

	private fun openOrThrow() = fopen(path, "r") ?: run {
		perror(path)
		error("Unable to open file at path $path")
	}

	public companion object {
		public fun forRelativeOrAbsolutePath(path: String): File = File(
			path = if (path.startsWith("/")) path else "$baseDir/$path"
		)

		private val baseDir = get_current_dir_name()?.toKStringFromUtf8()
	}
}
