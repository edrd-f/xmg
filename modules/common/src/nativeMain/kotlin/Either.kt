package io.gitlab.edrd.xmousegrabber.common

public sealed class Either<out A, out B> {
	public abstract val isLeft: Boolean
	public abstract val isRight: Boolean

	public abstract val left: A
	public abstract val right: B

	public data class Left<out A>(val value: A) : Either<A, Nothing>() {
		override val isLeft: Boolean = true
		override val isRight: Boolean = false
		override val left: A get() = value
		override val right: Nothing get() = error("Attempting to get Right value of Either.Left")
	}

	public data class Right<out B>(val value: B) : Either<Nothing, B>() {
		override val isLeft: Boolean = false
		override val isRight: Boolean = true
		override val left: Nothing get() = error("Attempting to get Right value of Either.Left")
		override val right: B get() = value
	}

	public companion object
}

public inline fun <T> T.left(): Either<T, Nothing> = Either.Left(this)

public inline fun <T> T.right(): Either<Nothing, T> = Either.Right(this)

public inline fun <A, B : Any> B?.rightIfNotNull(left: () -> A): Either<A, B> = when (this) {
	null -> left.invoke().left()
	else -> this.right()
}

public inline fun <A, B> Either<A, B>.rightOr(block: (A) -> Nothing): B {
	if (isLeft) block(left)
	return right
}

public inline fun <A, B, C> Either<A, B>.map(transform: (B) -> C): Either<A, C> = when (this) {
	is Either.Left -> value.left()
	is Either.Right -> transform(value).right()
}

public inline fun <A, B, C> Either<A, B>.mapLeft(transform: (A) -> C): Either<C, B> = when (this) {
	is Either.Left -> transform(value).left()
	is Either.Right -> value.right()
}

public inline fun <A, B, C> Either<A, B>.flatMap(transform: (B) -> Either<A, C>): Either<A, C> = when (this) {
	is Either.Left -> value.left()
	is Either.Right -> transform(value)
}

public inline operator fun <A, B> Either<A, List<B>>.plus(other: B): Either<A, List<B>> = map { it + other }
