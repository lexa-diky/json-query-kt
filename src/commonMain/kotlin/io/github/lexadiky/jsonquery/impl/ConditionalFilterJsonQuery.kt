package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmInline

@JvmInline
internal value class ConditionalFilterJsonQuery(
    private val predicate: (JsonElement) -> Boolean
) : JsonQuery {
    override fun select(json: JsonElement): JsonElement {
        return when (json) {
            is JsonArray -> {
                JsonArray(
                    json.filterIsInstance<JsonElement>()
                        .filter(predicate)
                )
            }
            else -> {
                if (json is JsonPrimitive && predicate(json)) json else JsonNull
            }
        }
    }

    override fun toString(): String {
        return "filter()"
    }
}
