package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@JvmInline
internal value class FinalizedJsonQuery(internal val query: JsonQuery) : JsonQuery {
    override fun select(json: JsonElement): JsonElement {
        return query.select(json)
    }

    override fun toString(): String = "query($query)"
}
