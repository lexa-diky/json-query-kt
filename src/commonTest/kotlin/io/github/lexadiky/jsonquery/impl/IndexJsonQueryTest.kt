package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlin.test.Test
import kotlin.test.assertEquals

class IndexJsonQueryTest {

    @Test
    fun `index form array`() {
        val element = buildJsonArray {
            add(JsonPrimitive("first"))
            add(JsonPrimitive("second"))
            add(JsonPrimitive("third"))
        }

        val resolved = element.query {
            get(1)
        }

        assertEquals(resolved, JsonPrimitive("second"))
    }

    @Test
    fun `index form array out of range`() {
        val element = buildJsonArray {
            add(JsonPrimitive("first"))
            add(JsonPrimitive("second"))
            add(JsonPrimitive("third"))
        }

        val resolved = element.query {
            get(33)
        }

        assertEquals(resolved, JsonNull)
    }
}
