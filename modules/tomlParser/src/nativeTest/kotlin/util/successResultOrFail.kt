package util

import io.gitlab.edrd.xmousegrabber.common.Either
import kotlin.test.fail

fun <A : Exception, B> Either<A, B>.successResultOrFail(): B = when (this) {
	is Either.Left -> {
		val actual = "${this.value::class.qualifiedName}(${this.value.message})"
		fail("Expected success result but got $actual instead")
	}
	is Either.Right -> this.value
}
