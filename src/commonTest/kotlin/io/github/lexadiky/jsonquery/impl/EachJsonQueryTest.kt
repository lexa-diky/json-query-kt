package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class EachJsonQueryTest {
    @Test
    fun `applies query to each element in array`() {
        val query = EachJsonQuery(object : JsonQuery {
            override fun select(json: JsonElement): JsonElement =
                if (json is JsonPrimitive && json.isString) JsonPrimitive(json.content.uppercase()) else json
        })
        val input = JsonArray(listOf(JsonPrimitive("a"), JsonPrimitive("b"), JsonPrimitive("c")))
        val result = query.select(input)
        assertEquals(JsonArray(listOf(JsonPrimitive("A"), JsonPrimitive("B"), JsonPrimitive("C"))), result)
    }

    @Test
    fun `applies query to single element if not array`() {
        val query = EachJsonQuery(object : JsonQuery {
            override fun select(json: JsonElement): JsonElement = JsonPrimitive("X")
        })
        val input = JsonPrimitive("a")
        val result = query.select(input)
        assertEquals(JsonPrimitive("X"), result)
    }

    @Test
    fun `returns empty array when input is empty array`() {
        val query = EachJsonQuery(object : JsonQuery {
            override fun select(json: JsonElement): JsonElement = JsonPrimitive("Y")
        })
        val input = JsonArray(emptyList())
        val result = query.select(input)
        assertEquals(JsonArray(emptyList()), result)
    }

    @Test
    fun `handles nulls in array`() {
        val query = EachJsonQuery(object : JsonQuery {
            override fun select(json: JsonElement): JsonElement = if (json is JsonNull) JsonPrimitive("null") else json
        })
        val input = JsonArray(listOf(JsonNull, JsonPrimitive("b")))
        val result = query.select(input)
        assertEquals(JsonArray(listOf(JsonPrimitive("null"), JsonPrimitive("b"))), result)
    }
}

