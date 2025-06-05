package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

@JvmInline
internal value class FinalizedJsonQuery(private val query: JsonQuery) : JsonQuery {
    override fun resolve(json: JsonElement): JsonElement {
        return query.resolve(json)
    }

    override fun toString(): String = "query($query)"
}