package io.github.lexadiky.jsonquery.util

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

fun JsonArray.mapJsonNotNull(element: (JsonElement) -> JsonElement): JsonArray {
    return JsonArray(
        this.map { element -> element(element) }
            .filter { it != JsonNull }
    )
}
