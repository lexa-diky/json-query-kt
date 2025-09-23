package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class AndPathJsonQueryTest {

    @Test
    fun `and at root returns subset when all present`() {
        val element = buildJsonObject {
            put("a", 1)
            put("b", 2)
            put("c", 3)
        }

        val result = element.query { path("a&b") }
        val expected = buildJsonObject {
            put("a", 1)
            put("b", 2)
        }
        assertEquals(expected, result)
    }

    @Test
    fun `and at root returns null when any missing`() {
        val element = buildJsonObject {
            put("a", 1)
        }

        val result = element.query { path("a&b") }
        assertEquals(JsonNull, result)
    }

    @Test
    fun `and continues with following segments`() {
        val element = buildJsonObject {
            putJsonObject("a") { put("x", 1) }
            putJsonObject("b") { put("x", 2) }
        }

        val result = element.query { path("a&b.x") }
        val expected = buildJsonObject {
            put("a", 1)
            put("b", 2)
        }
        assertEquals(expected, result)
    }

    @Test
    fun `and within arrays spreads and filters elements`() {
        val element = buildJsonObject {
            putJsonArray("arr") {
                addJsonObject { put("a", 1); put("b", 10) }
                addJsonObject { put("a", 2) } // missing b -> filtered out
                addJsonObject { put("b", 30) } // missing a -> filtered out
                addJsonObject { put("a", 3); put("b", 33) }
            }
        }

        val result = element.query { path("arr.a&b") }
        val expected = buildJsonArray {
            add(buildJsonObject { put("a", 1); put("b", 10) })
            add(buildJsonObject { put("a", 3); put("b", 33) })
        }
        assertEquals(JsonArray(expected), result)
    }

    @Test
    fun `and combined with wildcard spreads over objects and filters`() {
        val element = buildJsonObject {
            putJsonObject("left") { put("a", 10); put("b", 20) }
            putJsonObject("right") { put("a", 100) } // missing b -> removed
        }

        val result = element.query { path("*.a&b") }
        val expected = buildJsonObject {
            put("left", buildJsonObject { put("a", 10); put("b", 20) })
        }
        assertEquals(expected, result)
    }
}
