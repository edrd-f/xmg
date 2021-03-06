package io.gitlab.edrd.xmousegrabber.toml

// TODO: extract to common module
public sealed class Either<out A, out B> {
	public data class Left<out A>(val value: A) : Either<A, Nothing>()
	public data class Right<out B>(val value: B) : Either<Nothing, B>()
}

public fun <A : Exception, B> Either<A, B>.rightOrThrow(): B = when (this) {
	is Either.Left -> throw this.value
	is Either.Right -> this.value
}
