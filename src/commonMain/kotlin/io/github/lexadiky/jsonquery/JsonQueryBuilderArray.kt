package io.github.lexadiky.jsonquery

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlin.collections.first
import kotlin.collections.last

private fun JsonQueryBuilder.mapArray(onEmpty: JsonElement, fn: (List<JsonElement>) -> JsonElement) =
    map { element ->
        if (element is JsonArray) {
            if (element.isEmpty()) return@map onEmpty
            fn(element)
        } else {
            JsonNull
        }
    }

/**
 * Returns the first element of a JSON array.
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.first() =
    mapArray(JsonNull, List<JsonElement>::first)

/**
 * Returns the last element of a JSON array.
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.last() =
    mapArray(JsonNull, List<JsonElement>::last)

/**
 * Returns the size of a JSON array as a [JsonPrimitive].
 * Returns [JsonNull] if the input is not a JSON array.
 */
fun JsonQueryBuilder.size() =
    mapArray(JsonPrimitive(0)) { elements -> JsonPrimitive(elements.size) }

/**
 * Returns the deduplicated elements of a JSON array.
 */
fun JsonQueryBuilder.distinct() =
    mapArray(JsonArray(emptyList())) {
        JsonArray(it.distinct())
    }
