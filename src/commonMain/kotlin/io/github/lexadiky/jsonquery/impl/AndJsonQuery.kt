package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import io.github.lexadiky.jsonquery.util.mapJsonNotNull
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlin.jvm.JvmInline

/**
 * Selects a subset of properties only if all of them are present in the current object.
 * - When applied to JsonObject: returns a JsonObject containing only [properties] if all exist; otherwise JsonNull.
 * - When applied to JsonArray: applies the same rule to each element and returns an array of non-null results.
 * - Otherwise: returns JsonNull.
 */
@JvmInline
internal value class AndJsonQuery(
    private val properties: List<String>
) : JsonQuery {

    override fun select(json: JsonElement): JsonElement {
        return when (json) {
            is JsonObject -> {
                if (!properties.all { it in json }) return JsonNull
                JsonObject(json.filterKeys { it in properties })
            }

            is JsonArray -> {
                JsonArray(json.mapJsonNotNull { select(it) })
            }

            else -> JsonNull
        }
    }

    override fun toString(): String = properties.joinToString(" & ")
}
