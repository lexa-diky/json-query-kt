package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.MapJsonQuery
import kotlinx.serialization.json.JsonElement

/**
 * Maps each element in the current query using the provided [transform] function.
 *
 * This is a convenient alias for [JsonQueryBuilder.map], allowing for concise mapping of JSON elements.
 *
 * Example usage:
 * ```kotlin
 * element.query {
 *     qmap {
 *         buildJsonObject {
 *             put("first_name", query { path("name") })
 *             put("primary_skill", query { path("skills")[0] })
 *         }
 *     }
 * }
 * ```
 */
fun JsonQueryBuilder.qmap(transform: JsonElement.() -> JsonElement): JsonQueryBuilder =
    join(MapJsonQuery { element -> element.transform() })
