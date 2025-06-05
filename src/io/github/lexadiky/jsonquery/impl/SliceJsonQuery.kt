package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull


@JvmInline
internal value class SliceJsonQuery(private val range: IntRange): JsonQuery {
    override fun resolve(json: JsonElement): JsonElement {
        if (json !is JsonArray) return JsonNull

        val size = json.size
        val step = range.step

        if (step == 0) return JsonArray(emptyList()) // step must not be zero

        val rawStart = range.start
        val rawEnd = range.endInclusive

        // Normalize start
        val start = when {
            rawStart < 0 -> (size + rawStart).coerceIn(0, size)
            else -> rawStart.coerceIn(0, size)
        }

        // Normalize end (inclusive to exclusive)
        val end = when {
            rawEnd < 0 -> (size + rawEnd + 1).coerceIn(0, size)
            else -> (rawEnd + 1).coerceIn(0, size)
        }

        // Slice safely
        val sliced = try {
            json.slice(start until end step step)
        } catch (e: IllegalArgumentException) {
            emptyList()
        }

        return JsonArray(sliced)
    }

    override fun toString(): String {
        return "[${range.start}, ${range.endInclusive}${
            if (range.step != 1) ", ${range.step}" else ""
        }]"
    }
}