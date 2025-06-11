package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmInline
import kotlin.reflect.typeOf

@JvmInline
value class JsonQueryBuilder(@PublishedApi internal val parent: JsonQuery? = null) {

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

    operator fun get(index: Int): JsonQueryBuilder = buildup {
        IndexJsonQuery(index)
    }

    operator fun get(range: IntRange): JsonQueryBuilder = buildup {
        SliceJsonQuery(range)
    }

    fun filter(condition: (JsonElement) -> Boolean): JsonQueryBuilder = buildup {
        ConditionalFilterJsonQuery(condition)
    }

    inline fun <reified T> filterT(noinline condition: (T) -> Boolean): JsonQueryBuilder = buildup {
        ConditionalTypedFilterJsonQuery(typeOf<T>(), condition)
    }

    fun select(vararg properties: String): JsonQueryBuilder = buildup {
        SelectJsonQuery(properties.toList())
    }

    fun flatten(recursive: Boolean = false): JsonQueryBuilder = buildup {
        FlattenJsonQuery(recursive)
    }

    fun map(transform: (JsonElement) -> JsonElement): JsonQueryBuilder = buildup {
        MapJsonQuery(transform)
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
