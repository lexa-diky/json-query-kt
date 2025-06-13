package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.ConditionalFilterJsonQuery
import io.github.lexadiky.jsonquery.impl.ConditionalTypedFilterJsonQuery
import io.github.lexadiky.jsonquery.impl.EachJsonQuery
import io.github.lexadiky.jsonquery.impl.FlattenJsonQuery
import io.github.lexadiky.jsonquery.impl.IdentityJsonQuery
import io.github.lexadiky.jsonquery.impl.IndexJsonQuery
import io.github.lexadiky.jsonquery.impl.JoinJsonQuery
import io.github.lexadiky.jsonquery.impl.MapJsonQuery
import io.github.lexadiky.jsonquery.impl.MapTypedJsonQuery
import io.github.lexadiky.jsonquery.impl.PathJsonQuery
import io.github.lexadiky.jsonquery.impl.SelectJsonQuery
import io.github.lexadiky.jsonquery.impl.SliceJsonQuery
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline
import kotlin.reflect.typeOf

/**
 * A builder for composing JSON queries in a fluent, type-safe DSL style.
 *
 * Each method returns a new [JsonQueryBuilder] with the next query operation appended.
 * Queries can be chained to traverse, filter, map, and transform JSON data.
 *
 * Example usage:
 * ```kotlin
 * val result = json.query {
 *     path("users")
 *     filter { it.jsonPrimitive.int > 18 }
 *     select("name", "email")
 * }
 * ```
 *
 * @property parent The current composed [JsonQuery] (internal).
 */
@JvmInline
@Suppress("TooManyFunctions")
value class JsonQueryBuilder(@PublishedApi internal val parent: JsonQuery? = null) {

    /**
     * Traverse into the given object [segments] (dot notation supported).
     * If chained, appends to the current path.
     */
    fun path(vararg segments: String): JsonQueryBuilder {
        if (parent is PathJsonQuery) {
            return JsonQueryBuilder(
                PathJsonQuery(
                    parent.segments + segments
                )
            )
        }

        val objPath = segments.flatMap { segment -> segment.split(".") }
        return join(PathJsonQuery(objPath))
    }

    /**
     * Selects the element at the given array [index].
     */
    operator fun get(index: Int): JsonQueryBuilder =
        join(IndexJsonQuery(index))

    /**
     * Traverse into the given object [segments] (dot notation supported).
     */
    operator fun get(vararg segments: String): JsonQueryBuilder =
        path(segments = segments)

    /**
     * Selects a slice of the array using the given [range].
     */
    operator fun get(range: IntRange): JsonQueryBuilder =
        join(SliceJsonQuery(range))

    /**
     * Filters array elements using the given [condition].
     */
    fun filter(condition: (JsonElement) -> Boolean): JsonQueryBuilder =
        join(ConditionalFilterJsonQuery(condition))

    /**
     * Filters array elements by type [T] and [condition].
     */
    inline fun <reified T> filterT(noinline condition: (T) -> Boolean): JsonQueryBuilder =
        join(ConditionalTypedFilterJsonQuery(typeOf<T>(), condition))

    /**
     * Selects the given [properties] from an object.
     */
    fun select(vararg properties: String): JsonQueryBuilder =
        join(SelectJsonQuery(properties.toList()))

    /**
     * Flattens nested arrays. If [recursive] is true, flattens all levels.
     */
    fun flatten(recursive: Boolean = false): JsonQueryBuilder =
        join(FlattenJsonQuery(recursive))

    /**
     * Maps each element using the given [transform] function.
     */
    fun map(transform: (JsonElement) -> JsonElement): JsonQueryBuilder =
        join(MapJsonQuery(transform))

    /**
     * Runs query on each element and maps the result using the given [fn] function.
     * If applied to non array elements, it will run on single element
     */
    fun each(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonQueryBuilder =
        join(EachJsonQuery(JsonQuery(fn)))

    /**
     * Maps each element using the given [transform] function and types [F], [T].
     */
    inline fun <reified F, reified T> mapT(noinline transform: (F) -> T): JsonQueryBuilder =
        join(MapTypedJsonQuery(typeOf<F>(), typeOf<T>(), transform))

    /**
     * Creates an identity query that returns the input JSON element unchanged.
     * This is useful for building custom queries that may not modify the input.
     */
    fun identity(): JsonQueryBuilder {
        if (parent is IdentityJsonQuery) {
            return this
        }

        return join(IdentityJsonQuery())
    }

    /**
     * Adds sub [JsonQuery] to this builder. Useful for creating custom query functions
     */
    fun join(query: JsonQuery): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinJsonQuery(parent, query))
        } else {
            JsonQueryBuilder(query)
        }
    }
}
