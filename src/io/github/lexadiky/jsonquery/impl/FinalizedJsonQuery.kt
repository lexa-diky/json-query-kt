package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@JvmInline
internal value class FinalizedJsonQuery(private val query: JsonQuery) : JsonQuery {
    override fun resolve(json: JsonElement): JsonElement {
        return query.resolve(json)
    }

    override fun toString(): String = "query($query)"
}