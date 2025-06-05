package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class SelectJsonQueryTest {
    @Test
    fun `select from object`() {
        val element = buildJsonObject {
            put("name", JsonPrimitive("John"))
            put("age", JsonPrimitive(24))
            put("city", JsonPrimitive("New York"))
        }

        val resolved = element.query {
            select("name", "age", "children")
        }

        assertEquals(
            buildJsonObject {
                put("name", JsonPrimitive("John"))
                put("age", JsonPrimitive(24))
            },
            resolved
        )
    }

    @Test
    fun `select empty form object`() {
        val element = buildJsonObject {
            put("name", JsonPrimitive("John"))
            put("age", JsonPrimitive(24))
            put("city", JsonPrimitive("New York"))
        }

        val resolved = element.query {
            select()
        }

        assertEquals(
            buildJsonObject {},
            resolved
        )
    }

    @Test
    fun `select from array`() {
        val element = buildJsonArray {
            add(
                buildJsonObject {
                    put("name", JsonPrimitive("John"))
                    put("age", JsonPrimitive(24))
                    put("city", JsonPrimitive("New York"))
                }
            )

            add(
                buildJsonObject {
                    put("name", JsonPrimitive("Victor"))
                    put("age", JsonPrimitive(62))
                    put("city", JsonPrimitive("Old York"))
                }
            )
        }

        val resolved = element.query {
            select("name")
        }

        assertEquals(
            buildJsonArray {
                add(buildJsonObject {
                    put("name", JsonPrimitive("John"))
                })
                add(buildJsonObject {
                    put("name", JsonPrimitive("Victor"))
                })
            },
            resolved
        )
    }
}