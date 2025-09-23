package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class ShortOrPathJsonQueryTest {

    @Test
    fun `or at root picks first present`() {
        val element = buildJsonObject {
            put("a", JsonPrimitive("first"))
            put("b", JsonPrimitive("second"))
        }

        val result = element.query { path("a|b") }
        assertEquals(JsonPrimitive("first"), result)
    }

    @Test
    fun `or at root picks second when first missing`() {
        val element = buildJsonObject {
            put("b", JsonPrimitive("second"))
        }

        val result = element.query { path("a|b") }
        assertEquals(JsonPrimitive("second"), result)
    }

    @Test
    fun `or at root returns null when none present`() {
        val element = buildJsonObject {
            put("c", JsonPrimitive("other"))
        }

        val result = element.query { path("a|b") }
        assertEquals(JsonNull, result)
    }

    @Test
    fun `or continues with following segments`() {
        val element = buildJsonObject {
            putJsonObject("a") { put("x", 1) }
            putJsonObject("b") { put("x", 2) }
        }

        val result1 = element.query { path("a|b.x") }
        assertEquals(JsonPrimitive(1), result1)

        val result2 = buildJsonObject {
            putJsonObject("b") { put("x", 2) }
        }.query { path("a|b.x") }
        assertEquals(JsonPrimitive(2), result2)
    }

    @Test
    fun `or within arrays spreads and selects per element`() {
        val element = buildJsonObject {
            putJsonArray("arr") {
                addJsonObject { put("a", 1) }
                addJsonObject { put("b", 2) }
                addJsonObject { put("a", 3); put("b", 99) }
                addJsonObject { /* neither a nor b */ }
            }
        }

        val result = element.query { path("arr.a|b") }
        // Expected: [1, 2, 3] (nulls are filtered out by ArraySpreadJsonQuery)
        val expected = buildJsonArray {
            add(JsonPrimitive(1))
            add(JsonPrimitive(2))
            add(JsonPrimitive(3))
        }
        assertEquals(JsonArray(expected), result)
    }

    @Test
    fun `or combined with wildcard spreads over objects`() {
        val element = buildJsonObject {
            putJsonObject("left") { put("a", 10) }
            putJsonObject("right") { put("b", 20) }
        }

        val result = element.query { path("*.a|b") }
        val expected = buildJsonObject {
            put("left", 10)
            put("right", 20)
        }
        assertEquals(expected, result)
    }
}
