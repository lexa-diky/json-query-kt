package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

@JvmInline
internal value class MapJsonQuery(private val transform: (JsonElement) -> JsonElement) : JsonQuery {

    override fun resolve(json: JsonElement): JsonElement {
        return transform(json)
    }
}
