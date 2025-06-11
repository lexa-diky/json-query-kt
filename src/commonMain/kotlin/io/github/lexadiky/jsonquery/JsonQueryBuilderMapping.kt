package io.github.lexadiky.jsonquery

import io.github.lexadiky.jsonquery.impl.MapJsonQuery
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject

fun JsonQueryBuilder.qmap(transform: JsonElement.() -> JsonElement): JsonQueryBuilder = buildup {
    MapJsonQuery { element -> element.transform() }
}

fun main() {
    val element = Json.parseToJsonElement(
        """
        {
            "name": "John",
            "age": 30,
            "city": "New York",
            "skills": ["Java", "Kotlin", "Python"]
        }
    """
    )

    element.query {
        qmap {
            buildJsonObject {
                put("first_name", query { path("name") })
                put("primary_skill", query { path("skills")[0] })
            }
        }
    }
}
