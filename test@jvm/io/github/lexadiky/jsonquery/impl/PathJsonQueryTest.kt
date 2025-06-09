package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonObject
import kotlin.test.*

class PathJsonQueryTest {

    @Test
    fun `element from root`() {
        val element = buildJsonObject {
            put("pathElement", JsonPrimitive("hello"))
        }

        val resolved  = element.query {
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

        val resolved  = element.query {
            path("some", "other")
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

        val resolved  = element.query {
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

        val resolved  = element.query {
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

        val resolved  = element.query {
            path("some.otherNonExistent.a.b.c.d")
        }

        assertEquals(JsonNull, resolved)
    }

    @Test
    fun `joining multiple path segments, optimization`() {
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

        val resolved  = query.resolve(element)

        assertEquals(JsonPrimitive("value"), resolved)

        assertIs<FinalizedJsonQuery>(query)
        assertIs<PathJsonQuery>(query.query)
    }
}
