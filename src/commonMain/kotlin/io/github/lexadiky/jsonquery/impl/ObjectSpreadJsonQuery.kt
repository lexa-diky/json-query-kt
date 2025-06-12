package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject

@JvmInline
internal value class ObjectSpreadJsonQuery(
    private val query: JsonQuery,
) : JsonQuery {

    override fun select(json: JsonElement): JsonElement {
        if (json !is JsonObject) return JsonNull

        return JsonObject(json.mapValues { (_, value) -> query.select(value) }
            .filterValues { it != JsonNull })
    }
}
