package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class MapTypedJsonQueryTest {
    @Test
    fun `nested object typed mapping`() {
        val element = buildJsonObject {
            put("d", JsonPrimitive(1))
        }

        val resolved = element.query {
            path("d")
                .mapT<Int, String> { (it + 2).toString() }
        }

        assertEquals(JsonPrimitive("3"), resolved)
    }

    @Test
    fun `maps string to int`() {
        val element = buildJsonObject {
            put("s", JsonPrimitive("hello"))
        }
        val resolved = element.query {
            path("s").mapT<String, Int> { it.length }
        }
        assertEquals(JsonPrimitive(5), resolved)
    }

    @Test
    fun `returns JsonNull for type mismatch`() {
        val element = buildJsonObject {
            put("n", JsonPrimitive("not an int"))
        }
        val resolved = element.query {
            path("n").mapT<Int, String> { it.toString() }
        }
        assertEquals(JsonPrimitive("not an int"), element["n"])
        assertEquals(JsonPrimitive("not an int"), element["n"])
        assertEquals(JsonNull, resolved)
    }

    @Test
    fun `returns JsonNull for null input`() {
        val resolved = buildJsonObject { }.query {
            path("missing").mapT<Int, String> { it.toString() }
        }
        assertEquals(JsonNull, resolved)
    }
}
