package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonArray

class FlattenJsonQuery(private val recursive: Boolean) : JsonQuery {
    override fun resolve(json: JsonElement): JsonElement {
        if (json !is JsonArray) return JsonNull

        if (recursive) {
            return JsonArray(flattenRecursive(json))
        }

        return JsonArray(
            json.flatMap { child ->
                if (child is JsonArray) {
                    child.jsonArray
                } else {
                    emptyList()
                }
            }
        )
    }

    private fun flattenRecursive(json: JsonElement): List<JsonElement> {
        if (json !is JsonArray) return emptyList()

        return json.flatMap { child ->
            if (child is JsonArray) {
                flattenRecursive(child)
            } else {
                listOf(child)
            }
        }
    }

    override fun toString(): String {
        return "flatten()"
    }
}
