package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import io.github.lexadiky.jsonquery.util.mapJsonNotNull
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlin.jvm.JvmInline

@JvmInline
internal value class SelectJsonQuery(
    private val properties: List<String>
) : JsonQuery {

    @Suppress("ReturnCount")
    override fun select(json: JsonElement): JsonElement {
        return when (json) {
            is JsonObject -> {
                JsonObject(
                    json.filterKeys { it in properties }
                )
            }

            is JsonArray -> {
                JsonArray(json.mapJsonNotNull { select(it) })
            }

            else -> {
                JsonNull
            }
        }
    }

    override fun toString(): String {
        return properties.joinToString(", ", prefix = "[", postfix = "]")
    }
}
