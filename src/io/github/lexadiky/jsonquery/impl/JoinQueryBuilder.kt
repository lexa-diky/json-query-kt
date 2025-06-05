package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement

internal class JoinQueryBuilder(
    private val parent: JsonQuery,
    private val child: JsonQuery
) : JsonQuery {

    override fun resolve(json: JsonElement): JsonElement {
        return parent.resolve(json).let(child::resolve)
    }

    override fun toString(): String {
        return "$parent.$child"
    }
}