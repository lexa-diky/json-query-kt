package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.FinalizedJsonQuery
import kotlinx.serialization.json.JsonElement

interface JsonQuery {
    fun select(json: JsonElement): JsonElement
}

fun JsonQuery(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonQuery {
    return FinalizedJsonQuery(
        JsonQueryBuilder().fn().parent
            ?: error("Query must have at least one element")
    )
}

fun JsonElement.query(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonElement {
    return JsonQuery(fn).select(this)
}
