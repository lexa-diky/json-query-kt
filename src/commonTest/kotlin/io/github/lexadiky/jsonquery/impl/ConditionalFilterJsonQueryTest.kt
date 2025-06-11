package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class ConditionalFilterJsonQueryTest {

    @Test
    fun `filter with condition`() {
        val element = buildJsonArray {
            add(JsonPrimitive(1))
            add(JsonPrimitive(2))
            add(JsonPrimitive(3))
            add(JsonPrimitive(4))
        }

        val resolved = element.query {
            filter { it.jsonPrimitive.content != "3"}
        }

        assertEquals(
            buildJsonArray {
                add(JsonPrimitive(1))
                add(JsonPrimitive(2))
                add(JsonPrimitive(4))
            },
            resolved
        )
    }
}
