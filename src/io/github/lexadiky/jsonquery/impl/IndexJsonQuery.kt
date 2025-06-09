package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlin.jvm.JvmInline

@JvmInline
internal value class IndexJsonQuery(
    private val index: Int = 0,
) : JsonQuery {

    override fun select(json: JsonElement): JsonElement {
        if (json is JsonArray) {
            return json.getOrNull(index) ?: JsonNull
        } else {
            return JsonNull
        }
    }

    override fun toString(): String {
        return "[$index]"
    }
}
