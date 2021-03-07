package io.gitlab.edrd.xmousegrabber.io

public val File.extension: String? get() =
	path
		.substringAfterLast('/')
		.substringAfterLast('.', missingDelimiterValue = "")
		.ifBlank { null }
