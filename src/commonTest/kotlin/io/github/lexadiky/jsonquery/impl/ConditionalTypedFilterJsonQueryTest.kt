package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonNull
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConditionalTypedFilterJsonQueryTest {
    @Test
    fun `filters even integers from array`() {
        val query = ConditionalTypedFilterJsonQuery<Int>(typeOf<Int>()) { it % 2 == 0 }
        val input = JsonArray(listOf(1, 2, 3, 4, 5, 6).map { JsonPrimitive(it) })
        val result = query.select(input)
        assertEquals(JsonArray(listOf(JsonPrimitive(2), JsonPrimitive(4), JsonPrimitive(6))), result)
    }

    @Test
    fun `filters string values containing a substring`() {
        val query = ConditionalTypedFilterJsonQuery<String>(typeOf<String>()) { it.contains("foo") }
        val input = JsonArray(listOf("foo", "bar", "foobar").map { JsonPrimitive(it) })
        val result = query.select(input)
        assertEquals(JsonArray(listOf(JsonPrimitive("foo"), JsonPrimitive("foobar"))), result)
    }

    @Test
    fun `returns JsonNull for non-matching single primitive`() {
        val query = ConditionalTypedFilterJsonQuery<Int>(typeOf<Int>()) { it > 10 }
        val input = JsonPrimitive(5)
        val result = query.select(input)
        assertEquals(JsonNull, result)
    }

    @Test
    fun `returns primitive for matching single primitive`() {
        val query = ConditionalTypedFilterJsonQuery<Int>(typeOf<Int>()) { it == 42 }
        val input = JsonPrimitive(42)
        val result = query.select(input)
        assertEquals(JsonPrimitive(42), result)
    }

    @Test
    fun `filters booleans correctly`() {
        val query = ConditionalTypedFilterJsonQuery<Boolean>(typeOf<Boolean>()) { it }
        val input = JsonArray(listOf(JsonPrimitive(true), JsonPrimitive(false), JsonPrimitive(true)))
        val result = query.select(input)
        assertEquals(JsonArray(listOf(JsonPrimitive(true), JsonPrimitive(true))), result)
    }

    @Test
    fun `returns empty array for no matches`() {
        val query = ConditionalTypedFilterJsonQuery<Int>(typeOf<Int>()) { it > 100 }
        val input = JsonArray(listOf(1, 2, 3).map { JsonPrimitive(it) })
        val result = query.select(input)
        assertTrue(result is JsonArray && result.isEmpty())
    }

    @Test
    fun `ignores non-primitive elements in array`() {
        val query = ConditionalTypedFilterJsonQuery<Int>(typeOf<Int>()) { it > 0 }
        val input = JsonArray(listOf(JsonPrimitive(1), JsonNull, JsonPrimitive(2)))
        val result = query.select(input)
        assertEquals(JsonArray(listOf(JsonPrimitive(1), JsonPrimitive(2))), result)
    }

    @Test
    fun dsl() {
        val element = JsonArray(listOf(1, 2, 3).map { JsonPrimitive(it) })
        val resolved = element.query {
            filterT<Int> { it > 1 }
        }

        assertEquals(
            JsonArray(listOf(2, 3).map { JsonPrimitive(it) }),
            resolved
        )
    }
}

