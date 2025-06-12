package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonArray
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals

class FlattenJsonQueryTest {

    @Test
    fun `flattening level 2`() {
        val element = buildJsonArray {
            repeat(5) {
                addJsonArray { add(JsonPrimitive(it)) }
            }
        }

        val resolved = element.query { flatten() }

        assertEquals(
            buildJsonArray { repeat(5) { add(JsonPrimitive(it)) } },
            resolved
        )
    }

    @Test
    fun `flattening level 3 recursive`() {
        val element = buildJsonArray {
            repeat(5) {
                addJsonArray {
                    repeat(5) {
                        addJsonArray {
                            add(JsonPrimitive(it))
                        }
                    }
                }
            }
        }

        val resolved = element.query { flatten(recursive = true) }

        assertEquals(
            JsonArray(
                listOf(0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4)
                    .map { JsonPrimitive(it) }),
            resolved
        )
    }

    @Test
    fun `flattening object`() {
        val element = buildJsonObject {
            put("names", buildJsonArray {
                add("John")
                add("Jane")
            })
            put("ages", buildJsonArray {
                add(20)
                add(25)
            })
        }

        assertEquals(
            buildJsonArray {
                add("John")
                add("Jane")
                add(20)
                add(25)
            },
            element.query { flatten() }
        )
    }

    @Test
    fun `flattening object recursive`() {
        val element = buildJsonObject {
            put("names", buildJsonArray {
                add("John")
                add("Jane")
            })
            put("ages", buildJsonArray {
                addJsonArray {
                    add(10)
                    add(20)
                    add(30)
                }
                addJsonObject {
                    put("drink", 21)
                    put("drive", 18)
                    put("a", buildJsonObject {
                        put("b", buildJsonObject {
                            put("c", 25)
                        })
                    })
                }
            })
        }

        assertEquals(
            buildJsonArray {
                add("John")
                add("Jane")
                add(10)
                add(20)
                add(30)
                add(21)
                add(18)
                add(25)
            },
            element.query { flatten(recursive = true) }
        )
    }
}
