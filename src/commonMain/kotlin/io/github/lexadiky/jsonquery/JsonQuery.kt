package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.FinalizedJsonQuery
import kotlinx.serialization.json.JsonElement

/**
 * Represents a query that can be executed on a [JsonElement] to produce a result.
 */
interface JsonQuery {
    /**
     * Applies the query to the given [json] element and returns the result as a [JsonElement].
     */
    fun select(json: JsonElement): JsonElement
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
fun JsonQuery(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonQuery {
    return FinalizedJsonQuery(
        JsonQueryBuilder().fn().parent
            ?: error("Query must have at least one element")
    )
}

/**
 * Executes a query, defined by [fn], on this [JsonElement].
 *
 * Example usage:
 * ```kotlin
 * val result = jsonElement.query { path("users").filter { ... } }
 * ```
 */
fun JsonElement.query(fn: JsonQueryBuilder.() -> JsonQueryBuilder): JsonElement {
    return JsonQuery(fn).select(this)
}

/**
 * Executes a query, defined [path], on this [JsonElement].
 *
 * Example usage:
 * ```kotlin
 * val result = jsonElement.query { path("users").filter { ... } }
 * ```
 */
fun JsonElement.query(vararg path: String): JsonElement {
    return query { path(segments = path) }
}
