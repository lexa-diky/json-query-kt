package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.MapJsonQuery
import kotlinx.serialization.json.JsonElement

fun JsonQueryBuilder.qmap(transform: JsonElement.() -> JsonElement): JsonQueryBuilder = buildup {
    MapJsonQuery { element -> element.transform() }
}
