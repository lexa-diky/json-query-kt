package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.addJsonArray
import kotlinx.serialization.json.buildJsonArray
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
}
