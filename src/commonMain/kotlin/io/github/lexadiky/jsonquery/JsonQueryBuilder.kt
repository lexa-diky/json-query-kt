package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.ConditionalFilterJsonQuery
import io.github.lexadiky.jsonquery.impl.ConditionalTypedFilterJsonQuery
import io.github.lexadiky.jsonquery.impl.EachJsonQuery
import io.github.lexadiky.jsonquery.impl.FlattenJsonQuery
import io.github.lexadiky.jsonquery.impl.IdentityJsonQuery
import io.github.lexadiky.jsonquery.impl.IndexJsonQuery
import io.github.lexadiky.jsonquery.impl.JoinQueryBuilder
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

        return buildup {
            val objPath = segments.flatMap { segment -> segment.split(".") }
            PathJsonQuery(objPath)
        }
    }

    /**
     * Selects the element at the given array [index].
     */
    operator fun get(index: Int): JsonQueryBuilder = buildup {
        IndexJsonQuery(index)
    }

    /**
     * Traverse into the given object [segments] (dot notation supported).
     */
    operator fun get(vararg segments: String): JsonQueryBuilder = path(segments = segments)

    /**
     * Selects a slice of the array using the given [range].
     */
    operator fun get(range: IntRange): JsonQueryBuilder = buildup {
        SliceJsonQuery(range)
    }

    /**
     * Filters array elements using the given [condition].
     */
    fun filter(condition: (JsonElement) -> Boolean): JsonQueryBuilder = buildup {
        ConditionalFilterJsonQuery(condition)
    }

    /**
     * Filters array elements by type [T] and [condition].
     */
    inline fun <reified T> filterT(noinline condition: (T) -> Boolean): JsonQueryBuilder = buildup {
        ConditionalTypedFilterJsonQuery(typeOf<T>(), condition)
    }

    /**
     * Selects the given [properties] from an object.
     */
    fun select(vararg properties: String): JsonQueryBuilder = buildup {
        SelectJsonQuery(properties.toList())
    }

    /**
     * Flattens nested arrays. If [recursive] is true, flattens all levels.
     */
    fun flatten(recursive: Boolean = false): JsonQueryBuilder = buildup {
        FlattenJsonQuery(recursive)
    }

    /**
     * Maps each element using the given [transform] function.
     */
    fun map(transform: (JsonElement) -> JsonElement): JsonQueryBuilder = buildup {
        MapJsonQuery(transform)
    }

    /**
     * Runs query on each element and maps the result using the given [transform] function.
     * If applied to non array elements, it will run on single element
     */
    fun each(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonQueryBuilder = buildup {
        EachJsonQuery(JsonQuery(fn))
    }

    /**
     * Maps each element using the given [transform] function and types [F], [T].
     */
    inline fun <reified F, reified T> mapT(noinline transform: (F) -> T): JsonQueryBuilder = buildup {
        MapTypedJsonQuery(typeOf<F>(), typeOf<T>(), transform)
    }

    fun identity(): JsonQueryBuilder = buildup {
        IdentityJsonQuery()
    }

    @PublishedApi
    internal inline fun buildup(fn: () -> JsonQuery): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, fn()))
        } else {
            JsonQueryBuilder(fn())
        }
    }
}
