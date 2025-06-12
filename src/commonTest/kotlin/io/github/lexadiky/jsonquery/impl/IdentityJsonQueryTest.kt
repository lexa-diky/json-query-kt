package io.github.lexadiky.jsonquery.impl

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class IdentityJsonQueryTest {
    @Test
    fun `returns the same primitive element`() {
        val input = JsonPrimitive("test")
        val query = IdentityJsonQuery()
        val result = query.select(input)
        assertEquals(input, result)
    }

    @Test
    fun `returns the same object element`() {
        val input = buildJsonObject { put("key", JsonPrimitive(123)) }
        val query = IdentityJsonQuery()
        val result = query.select(input)
        assertEquals(input, result)
    }

    @Test
    fun `returns the same array element`() {
        val input = JsonArray(listOf(JsonPrimitive(1), JsonPrimitive(2)))
        val query = IdentityJsonQuery()
        val result = query.select(input)
        assertEquals(input, result)
    }

    @Test
    fun `returns the same null element`() {
        val input = JsonNull
        val query = IdentityJsonQuery()
        val result = query.select(input)
        assertEquals(input, result)
    }
}

