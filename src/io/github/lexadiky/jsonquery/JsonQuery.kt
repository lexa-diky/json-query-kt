package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.FinalizedJsonQuery
import kotlinx.serialization.json.JsonElement

interface JsonQuery {
    fun resolve(json: JsonElement): JsonElement
}

fun JsonQuery(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonQuery {
    return FinalizedJsonQuery(JsonQueryBuilder().fn().parent!!)
}

fun JsonElement.query(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonElement {
    return JsonQuery(fn).resolve(this)
}
