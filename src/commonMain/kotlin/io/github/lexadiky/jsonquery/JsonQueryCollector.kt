package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.util.asTyped
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface JsonQueryCollector<T> {

    fun collect(element: JsonElement): T
}

@PublishedApi
internal class AutoJsonQueryCollector<T>(
    private val query: JsonQuery,
    private val type: KType
) : JsonQueryCollector<T> {

    @Suppress("UNCHECKED_CAST", "RemoveExplicitTypeArguments")
    override fun collect(element: JsonElement): T {
        val lookup = query.select(element)

        return if (type.isMarkedNullable) {
            lookup.asTyped<T>(type) as T
        } else {
            lookup.asTyped<T>(type)
                ?: error("Type missmatch, can't convert $lookup to $type")
        }
    }
}


/**
 * Builds a [JsonQuery] using a DSL-style [fn] block with [JsonQueryBuilder].
 *
 * Example usage:
 * ```kotlin
 * val query = JsonQuery {
 *     path("users").filter { ... }
 * }
 * ```
 */
inline fun <reified T> JsonQueryAs(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonQueryCollector<T> {
    val builder = JsonQueryBuilder().fn()
    return AutoJsonQueryCollector(
        builder.parent
            ?: builder.identity().parent
            ?: error("No query defined"),
        typeOf<T>()
    )
}

inline fun <reified T> JsonElement.queryAs(
    fn: JsonQueryBuilder.() -> JsonQueryBuilder
): T {
    return JsonQueryAs<T>(fn).collect(this)
}
