package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@JvmInline
internal value class EachJsonQuery(
    private val query: JsonQuery,
) : JsonQuery {

    override fun select(json: JsonElement): JsonElement {
        return if (json is JsonArray) {
            JsonArray(json.map { element -> query.select(element) })
        } else {
            query.select(json)
        }
    }
}
