package io.gitlab.edrd.xmousegrabber.config.internal

import io.gitlab.edrd.xmousegrabber.common.*
import io.gitlab.edrd.xmousegrabber.config.ConfigurationEither
import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration.*
import io.gitlab.edrd.xmousegrabber.toml.TomlParser
import io.gitlab.edrd.xmousegrabber.toml.TomlRootTable

internal object ConfigurationLoader
{
	fun load(tomlString: String): ConfigurationEither {
		return parser.parse(tomlString)
			.mapLeft { exception -> ParseError(exception.message ?: "Unknown parse error") }
			.flatMap(::loadConfiguration)
	}

	private fun loadConfiguration(rootTable: TomlRootTable): ConfigurationEither {
		return rootTable.getLong("version")
			.rightIfNotNull(left = { MissingOrInvalidVersion })
			.flatMap(::getConfigurationLoaderForVersion)
			.flatMap { loader -> loader.load(rootTable) }
	}

	private fun getConfigurationLoaderForVersion(version: Long) = when (version) {
		1L -> V1ConfigurationLoader().right()
		else -> UnsupportedVersion(version).left()
	}

	private val parser = TomlParser()
}
