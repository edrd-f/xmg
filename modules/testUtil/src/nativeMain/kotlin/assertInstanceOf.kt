package io.gitlab.edrd.xmousegrabber.testUtil

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
public inline fun <reified T> assertInstanceOf(value: Any) {
	contract {
		returns() implies (value is T)
	}
	assert(value is T) {
		"Expected value to be of type ${T::class.qualifiedName}, " +
			"got ${value::class.qualifiedName} instead"
	}
}
