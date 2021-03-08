package io.gitlab.edrd.xmousegrabber.config

import io.gitlab.edrd.xmousegrabber.common.Either
import io.gitlab.edrd.xmousegrabber.config.internal.ConfigurationLoader

public typealias ConfigurationEither = Either<InvalidConfiguration, Configuration>

public data class Configuration(val version: Double, val buttons: List<Button>)
{
	public data class Button(val number: Int, val command: String)

	public companion object
	{
		public fun loadFromToml(tomlString: String): ConfigurationEither = ConfigurationLoader.load(tomlString)
	}
}
