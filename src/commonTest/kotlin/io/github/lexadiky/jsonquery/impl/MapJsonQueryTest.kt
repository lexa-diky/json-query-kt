package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.putJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class MapJsonQueryTest {

    @Test
    fun `nested object mapping`() {
        val element = buildJsonObject {
            putJsonObject("a") {
                putJsonObject("b") {
                    putJsonObject("c") {
                        put("d", JsonPrimitive("1"))
                    }
                }
            }
        }

        val resolved = element.query {
            path("a.b.c.d")
                .map { element -> JsonPrimitive(element.jsonPrimitive.content + "2") }
        }

        assertEquals(JsonPrimitive("12"), resolved)
    }
}
