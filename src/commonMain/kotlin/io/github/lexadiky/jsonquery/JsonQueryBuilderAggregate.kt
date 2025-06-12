package io.github.lexadiky.jsonquery

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

private fun JsonQueryBuilder.aggregateDouble(fn: (List<Double>) -> Double) = map { element ->
    if (element is JsonArray) {
        val numbers = element.mapNotNull { (it as? JsonPrimitive)?.doubleOrNull }
        JsonPrimitive(fn(numbers))
    } else {
        JsonNull
    }
}

private fun JsonQueryBuilder.aggregate(fn: (List<JsonElement>) -> JsonElement) = map { element ->
    if (element is JsonArray) {
        fn(element)
    } else {
        JsonNull
    }
}

fun JsonQueryBuilder.min() = aggregateDouble(List<Double>::min)
fun JsonQueryBuilder.max() = aggregateDouble(List<Double>::max)
fun JsonQueryBuilder.average() = aggregateDouble(List<Double>::average)
fun JsonQueryBuilder.sum() = aggregateDouble(List<Double>::sum)
fun JsonQueryBuilder.first() = aggregate(List<JsonElement>::first)
fun JsonQueryBuilder.last() = aggregate(List<JsonElement>::last)
fun JsonQueryBuilder.size() = map { (it as? JsonArray)?.size?.let(::JsonPrimitive) ?: JsonNull }

