package io.github.lexadiky.jsonquery

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

private fun JsonQueryBuilder.aggregateDouble(fn: (List<Double>) -> Double) = map { element ->
    if (element is JsonArray) {
        if (element.isEmpty()) return@map JsonNull
        val numbers = element.mapNotNull { (it as? JsonPrimitive)?.doubleOrNull }
        JsonPrimitive(fn(numbers))
    } else {
        JsonNull
    }
}

private fun JsonQueryBuilder.aggregate(onEmpty: JsonPrimitive, fn: (List<JsonElement>) -> JsonElement) =
    map { element ->
        if (element is JsonArray) {
            if (element.isEmpty()) return@map onEmpty
            fn(element)
        } else {
            JsonNull
        }
    }

/**
 * Returns the minimum value in a JSON array as a [JsonPrimitive].
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.min() = aggregateDouble(List<Double>::min)

/**
 * Returns the maximum value in a JSON array as a [JsonPrimitive].
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.max() = aggregateDouble(List<Double>::max)

/**
 * Returns the average value in a JSON array as a [JsonPrimitive].
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.average() = aggregateDouble(List<Double>::average)

/**
 * Returns the sum of values in a JSON array as a [JsonPrimitive].
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.sum() = aggregateDouble(List<Double>::sum)

/**
 * Returns the first element of a JSON array.
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.first() = aggregate(JsonNull, List<JsonElement>::first)

/**
 * Returns the last element of a JSON array.
 * Returns [JsonNull] if the input is not a JSON array or is empty.
 */
fun JsonQueryBuilder.last() = aggregate(JsonNull, List<JsonElement>::last)

/**
 * Returns the size of a JSON array as a [JsonPrimitive].
 * Returns [JsonNull] if the input is not a JSON array.
 */
fun JsonQueryBuilder.size() = aggregate(JsonPrimitive(0)) { elements -> JsonPrimitive(elements.size) }
