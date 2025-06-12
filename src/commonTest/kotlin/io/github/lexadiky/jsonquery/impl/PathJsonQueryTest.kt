package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonArray
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PathJsonQueryTest {

    @Test
    fun `element from root`() {
        val element = buildJsonObject {
            put("pathElement", JsonPrimitive("hello"))
        }

        val resolved = element.query {
            path("pathElement")
        }

        assertEquals(JsonPrimitive("hello"), resolved)
    }

    @Test
    fun `element from complex path`() {
        val element = buildJsonObject {
            putJsonObject("some") {
                put("other", JsonPrimitive("hello"))
            }
        }

        val resolved = element.query {
            path("some", "other")
        }

        assertEquals(JsonPrimitive("hello"), resolved)
    }

    @Test
    fun `element from complex path with accessor dsl`() {
        val element = buildJsonObject {
            putJsonObject("some") {
                put("other", JsonPrimitive("hello"))
            }
        }

        val resolved = element.query {
            this["some", "other"]
        }

        assertEquals(JsonPrimitive("hello"), resolved)
    }

    @Test
    fun `element from complex path dot separated`() {
        val element = buildJsonObject {
            putJsonObject("some") {
                put("other", JsonPrimitive("hello"))
            }
        }

        val resolved = element.query {
            path("some.other")
        }

        assertEquals(JsonPrimitive("hello"), resolved)
    }

    @Test
    fun `element to non existent`() {
        val element = buildJsonObject {
            putJsonObject("some") {
                put("other", JsonPrimitive("hello"))
            }
        }

        val resolved = element.query {
            path("some.otherNonExistent")
        }

        assertEquals(JsonNull, resolved)
    }

    @Test
    fun `element to non existent long path`() {
        val element = buildJsonObject {
            putJsonObject("some") {
                put("other", JsonPrimitive("hello"))
            }
        }

        val resolved = element.query {
            path("some.otherNonExistent.a.b.c.d")
        }

        assertEquals(JsonNull, resolved)
    }

    @Test
    fun `joining multiple path segments optimization`() {
        val element = buildJsonObject {
            putJsonObject("a") {
                putJsonObject("b") {
                    put("c", JsonPrimitive("value"))
                }
            }
        }

        val query = JsonQuery {
            path("a").path("b").path("c")
        }

        val resolved = query.select(element)

        assertEquals(JsonPrimitive("value"), resolved)

        assertIs<PathJsonQuery>(query)
    }

    @Test
    fun `index access`() {
        val element = buildJsonObject {
            putJsonObject("a") {
                putJsonArray("b") {
                    add(JsonPrimitive("0"))
                    add(JsonPrimitive("1"))
                    add(JsonPrimitive("2"))
                }
            }
        }


        val resolved = element.query { path("a.b.0") }

        assertEquals(JsonPrimitive("0"), resolved)
    }

    @Test
    fun `path with wildcard returns all nested values`() {
        val element = buildJsonObject {
            put("a", buildJsonObject {
                put("x", JsonPrimitive(1))
                put("y", JsonPrimitive(2))
            })
            put("b", buildJsonObject {
                put("x", JsonPrimitive(3))
                put("y", JsonPrimitive(4))
            })
        }
        val result = element.query { path("*.x") }
        assertEquals(
            buildJsonObject {
                put("a", 1)
                put("b", 3)
            },
            result
        )
    }

    @Test
    fun `path with wildcard returns all nested values with arrays`() {
        val element = buildJsonObject {
            putJsonArray("a") {
                repeat(3) {
                    addJsonObject {
                        put("x", 1)
                        put("y", 2)
                    }
                }
            }
            putJsonArray("b") {
                repeat(3) {
                    addJsonObject {
                        put("x", 4)
                        put("y", 5)
                    }
                }
            }
        }
        val result = element.query { path("*.x") }
        assertEquals(
            buildJsonObject {
                putJsonArray("a") {
                    repeat(3) { add(1) }
                }

                putJsonArray("b") {
                    repeat(3) { add(4) }
                }
            },
            result
        )
    }
}
