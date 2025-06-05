package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import io.github.lexadiky.jsonquery.util.mapJsonNotNull
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

@JvmInline
internal value class ArraySpreadJsonQuery(
    private val query: JsonQuery,
) : JsonQuery {
    override fun resolve(json: JsonElement): JsonElement {
        if (json !is JsonArray) return JsonNull
        return JsonArray(json.mapJsonNotNull { element -> query.resolve(element) })
    }
}