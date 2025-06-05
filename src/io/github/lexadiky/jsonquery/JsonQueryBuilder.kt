package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.ConditionalFilterJsonQuery
import io.github.lexadiky.jsonquery.impl.FlattenJsonQuery
import io.github.lexadiky.jsonquery.impl.IndexJsonQuery
import io.github.lexadiky.jsonquery.impl.JoinQueryBuilder
import io.github.lexadiky.jsonquery.impl.PathJsonQuery
import io.github.lexadiky.jsonquery.impl.SelectJsonQuery
import io.github.lexadiky.jsonquery.impl.SliceJsonQuery
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmInline

@JvmInline
value class JsonQueryBuilder(internal val parent: JsonQuery? = null) {

    fun path(vararg segments: String): JsonQueryBuilder {
        val objPath = segments.flatMap { segment -> segment.split(".") }
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, PathJsonQuery(objPath)))
        } else {
            JsonQueryBuilder(PathJsonQuery(objPath))
        }
    }

    operator fun get(index: Int): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, IndexJsonQuery(index)))
        } else {
            JsonQueryBuilder(IndexJsonQuery(index))
        }
    }

    operator fun get(range: IntRange): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, SliceJsonQuery(range)))
        } else {
            JsonQueryBuilder(SliceJsonQuery(range))
        }
    }

    fun filter(condition: (JsonPrimitive) -> Boolean): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, ConditionalFilterJsonQuery(condition)))
        } else {
            JsonQueryBuilder(ConditionalFilterJsonQuery(condition))
        }
    }

    fun select(vararg properties: String): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, SelectJsonQuery(properties.toList())))
        } else {
            JsonQueryBuilder(SelectJsonQuery(properties.toList()))
        }
    }

    fun flatten(): JsonQueryBuilder {
        return if (parent != null) {
            JsonQueryBuilder(JoinQueryBuilder(parent, FlattenJsonQuery()))
        } else {
            JsonQueryBuilder(FlattenJsonQuery())
        }
    }
}