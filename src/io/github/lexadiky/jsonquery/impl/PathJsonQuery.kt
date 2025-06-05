package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

@JvmInline
internal value class PathJsonQuery(private val segments: List<String>) : JsonQuery {
    override fun resolve(json: JsonElement): JsonElement {
        var current: JsonElement? = json
        for ((index, segment) in segments.withIndex()) {
            when (current) {
                null -> return JsonNull
                is JsonObject -> current = current.jsonObject[segment]
                is JsonArray -> return ArraySpreadJsonQuery(
                    PathJsonQuery(segments.subList(index, segments.size))
                ).resolve(current)

                else -> return JsonNull
            }
        }
        return current ?: JsonNull
    }

    override fun toString(): String {
        return segments.joinToString(".")
    }
}