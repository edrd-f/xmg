import io.gitlab.edrd.xmousegrabber.toml.Either
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.fail

// TODO: extract this to a testCommon module
@ExperimentalContracts
inline fun <reified T> assertIsInstance(value: Any) {
	contract {
		returns() implies (value is T)
	}
	assert(value is T) {
		"Expected value to be of type ${T::class.qualifiedName}, " +
			"got ${value::class.qualifiedName} instead"
	}
}

fun <A : Exception, B> Either<A, B>.successResultOrFail(): B = when (this) {
	is Either.Left -> {
		val actual = "${this.value::class.qualifiedName}(${this.value.message})"
		fail("Expected success result but got $actual instead")
	}
	is Either.Right -> this.value
}
