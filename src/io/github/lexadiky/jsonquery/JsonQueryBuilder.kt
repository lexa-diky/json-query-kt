package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.ConditionalFilterJsonQuery
import io.github.lexadiky.jsonquery.impl.FlattenJsonQuery
import io.github.lexadiky.jsonquery.impl.IndexJsonQuery
import io.github.lexadiky.jsonquery.impl.JoinQueryBuilder
import io.github.lexadiky.jsonquery.impl.MapJsonQuery
import io.github.lexadiky.jsonquery.impl.PathJsonQuery
import io.github.lexadiky.jsonquery.impl.SelectJsonQuery
import io.github.lexadiky.jsonquery.impl.SliceJsonQuery
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmInline

@JvmInline
value class JsonQueryBuilder(internal val parent: JsonQuery? = null) {

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

    fun filter(condition: (JsonPrimitive) -> Boolean): JsonQueryBuilder = buildup {
        ConditionalFilterJsonQuery(condition)
    }

    fun select(vararg properties: String): JsonQueryBuilder = buildup {
        SelectJsonQuery(properties.toList())
    }

    fun flatten(): JsonQueryBuilder = buildup {
        FlattenJsonQuery()
    }

    fun map(transform: (JsonElement) -> JsonElement): JsonQueryBuilder = buildup {
        MapJsonQuery(transform)
    }

    private inline fun buildup(fn: () -> JsonQuery): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, fn()))
        } else {
            JsonQueryBuilder(fn())
        }
    }
}
