package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class ConditionalTypedFilterJsonQuery<T>(
    private val type: KType,
    private val predicate: (T) -> Boolean
) : JsonQuery {
    override fun select(json: JsonElement): JsonElement {
        return when (json) {
            is JsonArray -> {
                JsonArray(
                    json.filterIsInstance<JsonPrimitive>()
                        .filter { element -> element.asTyped()?.let(predicate) ?: false }
                )
            }

            else -> {
                if (json is JsonPrimitive && json.asTyped()?.let { predicate(it) } ?: false) {
                    json
                } else {
                    JsonNull
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun JsonElement.asTyped(): T? {
        if (this !is JsonPrimitive) return null

        return when (type) {
            typeOf<Int>() -> this.intOrNull
            typeOf<Long>() -> this.longOrNull
            typeOf<Boolean>() -> this.boolean
            typeOf<Float>() -> this.float
            typeOf<Double>() -> this.float.toDouble()
            typeOf<String>() -> this.content
            else -> null
        } as T?
    }

    override fun toString(): String {
        return "filter()"
    }
}
