package io.github.lexadiky.jsonquery

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonQueryBuilderAggregateTest {
    private fun array(vararg elements: String): JsonElement =
        JsonArray(elements.map { JsonPrimitive(it) })

    @Test
    fun min() {
        assertEquals(
            JsonPrimitive(1.0),
            array("1.0", "2.0", "3.0").query { min() }
        )
    }

    @Test
    fun max() {
        assertEquals(
            JsonPrimitive(3.0),
            array("1.0", "2.0", "3.0").query { max() }
        )
    }

    @Test
    fun average() {
        assertEquals(
            JsonPrimitive(2.0),
            array("1.0", "2.0", "3.0").query { average() }
        )
    }

    @Test
    fun sum() {
        assertEquals(
            JsonPrimitive(6.0),
            array("1.0", "2.0", "3.0").query { sum() }
        )
    }

    @Test
    fun first() {
        assertEquals(
            JsonPrimitive("1.0"),
            array("1.0", "2.0", "3.0").query { first() }
        )
    }

    @Test
    fun last() {
        assertEquals(
            JsonPrimitive("3.0"),
            array("1.0", "2.0", "3.0").query { last() }
        )
    }

    @Test
    fun size() {
        assertEquals(
            JsonPrimitive(3),
            array("1.0", "2.0", "3.0").query { size() }
        )
    }

    @Test
    fun emptyArray() {
        val empty = array()
        assertEquals(JsonNull, empty.query { min() })
        assertEquals(JsonNull, empty.query { max() })
        assertEquals(JsonNull, empty.query { average() })
        assertEquals(JsonNull, empty.query { sum() })
        assertEquals(JsonNull, empty.query { first() })
        assertEquals(JsonNull, empty.query { last() })
        assertEquals(JsonPrimitive(0), empty.query { size() })
    }
}
