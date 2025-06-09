package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement

internal class JoinQueryBuilder(
    private val parent: JsonQuery,
    private val child: JsonQuery
) : JsonQuery {

    override fun select(json: JsonElement): JsonElement {
        return parent.select(json).let(child::select)
    }

    override fun toString(): String {
        return "$parent.$child"
    }
}
