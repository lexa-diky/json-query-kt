package io.github.lexadiky.jsonquery.util

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

internal fun JsonArray.mapJsonNotNull(transform: (JsonElement) -> JsonElement): JsonArray {
    return JsonArray(
        this.map { transform(it) }
            .filter { it != JsonNull }
    )
}
