package io.gitlab.edrd.xmousegrabber.config.internal.v1

import io.gitlab.edrd.xmousegrabber.common.Either
import io.gitlab.edrd.xmousegrabber.common.left
import io.gitlab.edrd.xmousegrabber.common.right
import io.gitlab.edrd.xmousegrabber.config.Configuration
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration.DuplicateButtonNumber

internal fun validate(configuration: Configuration): Either<InvalidConfiguration, Configuration> {
	val (_, duplicates) = configuration.buttons.map { it.number }.partition(mutableSetOf<Int>()::add)

	if (duplicates.any())
		return DuplicateButtonNumber(number = duplicates.first()).left()

	return configuration.right()
}
