package io.gitlab.edrd.xmousegrabber.internal

import io.gitlab.edrd.xmousegrabber.config.InvalidConfiguration

fun messageForConfigurationError(error: InvalidConfiguration): String = when (error)
{
	InvalidConfiguration.MissingOrInvalidVersion -> "missing 'version' key"

	InvalidConfiguration.MissingOrInvalidMappingsType ->
		"missing or invalid type for 'mappings' key"

	is InvalidConfiguration.UnexpectedType ->
		"key ${error.location} must be of ${error.expected} type"

	is InvalidConfiguration.MissingOrInvalidButtonNumberType ->
		"missing or invalid type for 'button' key in ${error.location}"

	is InvalidConfiguration.MissingOrInvalidCommandType ->
		"missing or invalid type for 'command' key in ${error.location}"

	is InvalidConfiguration.UnsupportedVersion ->
		"version ${error.value} is not supported"

	is InvalidConfiguration.DuplicateButtonNumber ->
		"found duplicate button ${error.number} configuration"

	is InvalidConfiguration.ParseError -> error.message
}
