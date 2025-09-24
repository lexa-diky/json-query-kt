package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlin.jvm.JvmInline

@JvmInline
@Suppress("ReturnCount")
internal value class PathJsonQuery(internal val segments: List<String>) : JsonQuery {

    @Suppress("NestedBlockDepth", "CyclomaticComplexMethod")
    override fun select(json: JsonElement): JsonElement {
        var current: JsonElement? = json
        for ((index, segment) in segments.withIndex()) {
            when (current) {
                null -> return JsonNull
                is JsonObject -> {
                    if (segment == "*") {
                        return ObjectSpreadJsonQuery(
                            PathJsonQuery(segments.subList(index + 1, segments.size))
                        ).select(current)
                    } else if (segment.contains("|") || segment.contains("&")) {
                        // Mixed logical operators within a single segment.
                        // 'AND' has higher precedence than 'OR'. Evaluate OR options left-to-right.
                        val remaining = segments.subList(index + 1, segments.size)
                        val orOptions = segment.split("|")
                        for (option in orOptions) {
                            val optionResult = if (option.contains("&")) {
                                val keys = option.split("&")
                                val subset = AndJsonQuery(keys).select(current)
                                if (remaining.isEmpty()) {
                                    subset
                                } else {
                                    ObjectSpreadJsonQuery(PathJsonQuery(remaining)).select(subset)
                                }
                            } else {
                                PathJsonQuery(listOf(option) + remaining).select(current)
                            }
                            if (optionResult != JsonNull) {
                                return optionResult
                            }
                        }
                        return JsonNull
                    } else {
                        current = current.jsonObject[segment]
                    }
                }
                is JsonArray -> {
                    val segmentAsInt = segment.toIntOrNull()
                    if (segmentAsInt != null) {
                        current = current.jsonArray[segmentAsInt]
                    } else {
                        // Apply only the current non-integer segment to each array element,
                        // then continue processing remaining segments on the resulting array.
                        current = ArraySpreadJsonQuery(
                            PathJsonQuery(listOf(segment))
                        ).select(current)
                    }
                }

                else -> return JsonNull
            }
        }
        return current ?: JsonNull
    }

    override fun toString(): String {
        return segments.joinToString(".")
    }
}
