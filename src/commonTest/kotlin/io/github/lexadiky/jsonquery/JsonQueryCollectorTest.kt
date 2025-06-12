package io.github.lexadiky.jsonquery

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows
import java.lang.Exception
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JsonQueryCollectorTest {
    private val json = Json.parseToJsonElement(
        """
        {
            "shelter": {
                "name": "Happy Shelter",
                "cats": [
                    {"name": "Milo", "age": 2},
                    {"name": "Luna", "age": 3},
                    {"name": "Max", "age": 1}
                ]
            }
        }
        """
    )

    @Test
    fun `cquery returns typed list of names`() {
        val result = json.cquery<List<String>> { path("shelter.cats.name") }
        assertEquals(listOf("Milo", "Luna", "Max"), result)
    }

    @Test
    fun `cquery returns typed int`() {
        val result = json.cquery<Int> { path("shelter.cats.1.age") }
        assertEquals(3, result)
    }

    @Test
    fun `cquery returns typed map`() {
        val result = json.cquery<Map<String, Int>> {
            path("shelter.cats.1")
                .select("name", "age")
        }
        assertEquals(mapOf("age" to 3), result)
    }

    @Test
    fun `cquery throws for missing path for non nullable types`() {
        assertThrows<Exception> {
            json.cquery<String> { path("shelter.dogs.0.name") }
        }
    }

    @Test
    fun `cquery throws for type mismatch for non nullable types`() {
        assertThrows<Exception> {
            json.cquery<Int> { path("shelter.cats.0.name") }
        }
    }

    @Test
    fun `cquery returns null for missing path for non nullable types`() {
        assertNull(
            json.cquery<String?> { path("shelter.dogs.0.name") }
        )
    }

    @Test
    fun `cquery returns null for type mismatch for non nullable types`() {
        assertNull(
            json.cquery<Int?> { path("shelter.cats.0.name") }
        )

    }

    @Test
    fun `cquery works with mapT`() {
        val result = json.cquery<List<Int>> {
            path("shelter.cats.age").each {
                mapT<Int, Int> { it + 1 }
            }
        }
        assertEquals(listOf(3, 4, 2), result)
    }
}

