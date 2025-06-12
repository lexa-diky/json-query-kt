package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import io.github.lexadiky.jsonquery.util.asTyped
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlin.reflect.KType

@PublishedApi
internal class ConditionalTypedFilterJsonQuery<T>(
    private val type: KType,
    private val predicate: (T) -> Boolean
) : JsonQuery {
    override fun select(json: JsonElement): JsonElement {
        return when (json) {
            is JsonArray -> {
                JsonArray(
                    json.filterIsInstance<JsonPrimitive>()
                        .filter { element -> element.asTyped<T>(type)?.let(predicate) ?: false })
            }

            else -> {
                if (json is JsonPrimitive && json.asTyped<T>(type)?.let { predicate(it) } ?: false) {
                    json
                } else {
                    JsonNull
                }
            }
        }
    }

    override fun toString(): String {
        return "filter<$type>()"
    }
}
