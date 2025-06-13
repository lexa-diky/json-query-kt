package io.github.lexadiky.jsonquery

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonQueryBuilderMappingTest {

    @Test
    fun `object mapping`() {
        val json = Json.parseToJsonElement("""{"name":"Alice","skills":["Kotlin"]}""")
        val result = json.query {
            qmap {
                buildJsonObject {
                    put("first_name", query { path("name") })
                    put("primary_skill", query { path("skills")[0] })
                }
            }
        }
        val expected = buildJsonObject {
            put("first_name", JsonPrimitive("Alice"))
            put("primary_skill", JsonPrimitive("Kotlin"))
        }

        assertEquals(expected, result)
    }

    @Test
    fun `unrelated data`() {
        val json = Json.parseToJsonElement("[]")
        val result = json.query {
            qmap { JsonPrimitive("x") }
        }
        assertEquals(JsonPrimitive("x"), result)
    }
}

