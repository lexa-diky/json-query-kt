package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray

@JvmInline
internal value class FlattenJsonQuery(private val recursive: Boolean) : JsonQuery {

    @Suppress("ReturnCount")
    override fun select(json: JsonElement): JsonElement {
        return when (json) {
            is JsonArray -> flattenArray(json)
            is JsonObject -> flattenObject(json)
            else -> JsonNull
        }
    }

    private fun flattenObject(json: JsonObject): JsonArray {
        val flattened = mutableListOf<JsonElement>()

        json.forEach { (_, value) ->
            if (recursive && value !is JsonPrimitive) {
                flattened.add(select(value))
            } else {
                flattened.add(value)
            }
        }

        return flattenArray(JsonArray(flattened))
    }

    private fun flattenArray(json: JsonArray): JsonArray {
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
        if (json !is JsonArray) {
            return emptyList()
        }

        return json.flatMap { child ->
            if (child is JsonArray) {
                flattenRecursive(child)
            } else if (child is JsonObject) {
                flattenObject(child)
            } else {
                listOf(child)
            }
        }
    }

    override fun toString(): String {
        return "flatten()"
    }
}
