package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.query
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlin.test.Test
import kotlin.test.assertEquals

class ArraySpreadJsonQueryTest {

    @Test
    fun `array spread in range`() {
        val element = Json.parseToJsonElement("[1, 2, 3, 4, 5]")
        val resolved = element.query { get(0..4) }
        assertEquals(element, resolved)
    }

    @Test
    fun `array spread in range smaller`() {
        val element = Json.parseToJsonElement("[1, 2, 3, 4, 5]")
        val resolved = element.query { get(0..2) }
        assertEquals(Json.parseToJsonElement("[1, 2, 3]"), resolved)
    }

    @Test
    fun `array spread in empty`() {
        val element = Json.parseToJsonElement("[1, 2, 3, 4, 5]")
        val resolved = element.query { get(99..123) }
        assertEquals(buildJsonArray {  }, resolved)
    }
}