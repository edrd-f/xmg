package io.gitlab.edrd.xmousegrabber.properties

import io.gitlab.edrd.xmousegrabber.properties.internal.isOdd

public object Properties {
	public fun parse(text: String): Map<String, String> {
		return text
			.lineSequence()
			.filterNot { it.isBlank() || it.trimStart().startsWith('#') }
			.map(::EntryLine)
			.fold(listOf<EntryLine>()) { accumulator, line ->
				if (accumulator.any() && accumulator.last().continues) {
					accumulator.dropLast(1) + accumulator.last().join(line)
				} else accumulator + line
			}
			.map { it.toPair() }
			.toMap()
	}

	private data class EntryLine(private val content: String) {
		val continues get() = endsWithUnescapedBackslash(content)
		val contentExcludingLastBackslash get() = content.dropLast(1)

		fun join(other: EntryLine) = EntryLine(contentExcludingLastBackslash + other.content.trimStart())
		fun toPair() = key to value

		val key get() = split.first().trim()
		val value get() = split.getOrNull(1)?.let { rawValue ->
			if (endsWithUnescapedBackslash(rawValue)) rawValue.dropLast(1) else rawValue
		}?.trim() ?: ""

		private val split by lazy { content.split('=', limit = 2) }
		private fun endsWithUnescapedBackslash(str: String) = str.takeLastWhile { it == '\\' }.length.isOdd
	}
}
