package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.util.asTyped
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * A collector interface for collecting results from a [JsonElement] based on a [JsonQuery].
 *
 * Implementations of this interface should provide a way to collect and convert the selected
 * elements into the desired type [T].
 *
 * Commonly implementers have contractor with [JsonQuery] and [KType] parameters.
 */
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
 * Builds a [JsonQuery] using a DSL-style [query] block with [JsonQueryBuilder].
 *
 * Example usage:
 * ```kotlin
 * val query = JsonQuery {
 *     path("users").filter { ... }
 * }
 * ```
 */
@Suppress("FunctionNaming")
inline fun <reified T> JsonQueryAs(
    collector: (JsonQuery, KType) -> JsonQueryCollector<T> = ::AutoJsonQueryCollector,
    query: JsonQueryBuilder.() -> JsonQueryBuilder
): JsonQueryCollector<T> {
    val builder = JsonQueryBuilder().query()
    return collector(
        builder.parent
            ?: builder.identity().parent
            ?: error("No query defined"),
        typeOf<T>()
    )
}

/**
 * Executes a query, defined by [fn], on this [JsonElement].
 * And returns the converted result as type [T].
 *
 * Example usage:
 * ```kotlin
 * val result = jsonElement.queryAs<List<Int>> { path("users.age").filter { ... } }
 * ```
 */
inline fun <reified T> JsonElement.queryAs(
    collector: (JsonQuery, KType) -> JsonQueryCollector<T> = ::AutoJsonQueryCollector,
    fn: JsonQueryBuilder.() -> JsonQueryBuilder
): T {
    return JsonQueryAs<T>(collector, fn).collect(this)
}
