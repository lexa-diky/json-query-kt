package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement

internal class IdentityJsonQuery : JsonQuery {

    override fun select(json: JsonElement): JsonElement = json

    override fun toString(): String {
        return "identity()"
    }
}
