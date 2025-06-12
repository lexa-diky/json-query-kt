package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

@JvmInline
internal value class EachJsonQuery(
    private val query: JsonQuery,
) : JsonQuery {

    override fun select(json: JsonElement): JsonElement {
        if (json is JsonArray) {
            return JsonArray(json.map { element -> query.select(element) })
        } else {
            return query.select(json)
        }
    }
}
