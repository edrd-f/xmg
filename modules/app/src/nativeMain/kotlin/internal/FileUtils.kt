package io.gitlab.edrd.xmousegrabber.internal

import internal.cinterop.glibc.get_current_dir_name
import kotlinx.cinterop.toKStringFromUtf8

internal fun filePathForArgument(arg: String): String =
	if (arg.startsWith("/")) arg else "$baseDir/$arg"

private val baseDir = get_current_dir_name()?.toKStringFromUtf8()
