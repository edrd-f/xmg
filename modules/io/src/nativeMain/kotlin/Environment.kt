package io.gitlab.edrd.xmousegrabber.io

import kotlinx.cinterop.toKStringFromUtf8
import platform.posix.getenv

public object Environment {
	public operator fun get(name: String): String? = getenv(name)?.toKStringFromUtf8()
}
