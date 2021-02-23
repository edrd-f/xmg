package io.gitlab.edrd.xmousegrabber.internal

import com.soywiz.korio.file.std.LocalVfs
import internal.cinterop.glibc.get_current_dir_name
import kotlinx.cinterop.toKStringFromUtf8
import kotlinx.coroutines.runBlocking

internal fun filePathForArgument(arg: String): String =
	if (arg.startsWith("/")) arg else "$baseDir/$arg"

internal fun fileExists(path: String): Boolean = runBlocking {
	LocalVfs[path].exists()
}

private val baseDir = get_current_dir_name()?.toKStringFromUtf8()
