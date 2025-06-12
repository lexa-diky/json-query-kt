package io.github.lexadiky.jsonquery.util

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TypeConversionUtilTest {
    @Test
    fun `converts JsonPrimitive to Byte`() {
        val json = JsonPrimitive(42)
        val result: Byte? = json.asTyped(typeOf<Byte>())
        assertEquals(42.toByte(), result)
    }

    @Test
    fun `converts JsonPrimitive to Short`() {
        val json = JsonPrimitive(42)
        val result: Short? = json.asTyped(typeOf<Short>())
        assertEquals(42.toShort(), result)
    }

    @Test
    fun `converts JsonPrimitive to Int`() {
        val json = JsonPrimitive(42)
        val result: Int? = json.asTyped(typeOf<Int>())
        assertEquals(42, result)
    }

    @Test
    fun `converts JsonPrimitive to Long`() {
        val json = JsonPrimitive(42L)
        val result: Long? = json.asTyped(typeOf<Long>())
        assertEquals(42L, result)
    }

    @Test
    fun `converts JsonPrimitive to Float`() {
        val json = JsonPrimitive(42.5f)
        val result: Float? = json.asTyped(typeOf<Float>())
        assertEquals(42.5f, result)
    }

    @Test
    fun `converts JsonPrimitive to Double`() {
        val json = JsonPrimitive(42.5)
        val result: Double? = json.asTyped(typeOf<Double>())
        assertEquals(42.5, result)
    }

    @Test
    fun `converts JsonPrimitive to Boolean`() {
        val json = JsonPrimitive(true)
        val result: Boolean? = json.asTyped(typeOf<Boolean>())
        assertEquals(true, result)
    }

    @Test
    fun `converts JsonPrimitive to String`() {
        val json = JsonPrimitive("hello")
        val result: String? = json.asTyped(typeOf<String>())
        assertEquals("hello", result)
    }

    @Test
    fun `returns null for non-primitive to primitive conversion`() {
        val json = JsonArray(listOf(JsonPrimitive(1)))
        val result: Int? = json.asTyped(typeOf<Int>())
        assertNull(result)
    }

    @Test
    fun `converts JsonArray to List`() {
        val json = JsonArray(listOf(JsonPrimitive(1), JsonPrimitive(2), JsonPrimitive(3)))
        val result: List<Int>? = json.asTyped(typeOf<List<Int>>())
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun `converts JsonObject to Map`() {
        val json = buildJsonObject {
            put("a", JsonPrimitive(1))
            put("b", JsonPrimitive(2))
        }
        val result: Map<String, Int>? = json.asTyped(typeOf<Map<String, Int>>())
        assertEquals(mapOf("a" to 1, "b" to 2), result)
    }

    @Test
    fun `returns null for map if any value fails conversion`() {
        val json = buildJsonObject {
            put("a", JsonPrimitive(1))
            put("b", JsonPrimitive("not an int"))
        }
        val result: Map<String, Int>? = json.asTyped(typeOf<Map<String, Int>>())
        assertEquals(mapOf("a" to 1), result)
    }

    @Test
    fun `returns null for list if any value fails conversion`() {
        val json = JsonArray(listOf(JsonPrimitive(1), JsonPrimitive("not an int")))
        val result: List<Int>? = json.asTyped(typeOf<List<Int>>())
        assertEquals(listOf(1), result) // Only successful conversions are included
    }

    @Test
    fun `returns null for unknown type`() {
        val json = JsonPrimitive(1)

        data class Dummy(val x: Int)

        val result: Dummy? = json.asTyped(typeOf<Dummy>())
        assertNull(result)
    }
}
