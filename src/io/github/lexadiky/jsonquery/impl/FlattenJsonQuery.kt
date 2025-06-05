package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonArray

class FlattenJsonQuery : JsonQuery {
    override fun resolve(json: JsonElement): JsonElement {
        if (json !is JsonArray) return JsonNull

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

    override fun toString(): String {
        return "flatten()"
    }
}
