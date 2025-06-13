package io.github.lexadiky.jsonquery

import kotlinx.serialization.Serializable
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
    fun `queryAs returns typed list of names`() {
        val result = json.queryAs<List<String>> { path("shelter.cats.name") }
        assertEquals(listOf("Milo", "Luna", "Max"), result)
    }

    @Test
    fun `queryAs returns typed int`() {
        val result = json.queryAs<Int> { path("shelter.cats.1.age") }
        assertEquals(3, result)
    }

    @Test
    fun `queryAs returns typed map`() {
        val result = json.queryAs<Map<String, Int>> {
            path("shelter.cats.1")
                .select("name", "age")
        }
        assertEquals(mapOf("age" to 3), result)
    }

    @Test
    fun `queryAs throws for missing path for non nullable types`() {
        assertThrows<Exception> {
            json.queryAs<String> { path("shelter.dogs.0.name") }
        }
    }

    @Test
    fun `queryAs throws for type mismatch for non nullable types`() {
        assertThrows<Exception> {
            json.queryAs<Int> { path("shelter.cats.0.name") }
        }
    }

    @Test
    fun `queryAs returns null for missing path for non nullable types`() {
        assertNull(
            json.queryAs<String?> { path("shelter.dogs.0.name") }
        )
    }

    @Test
    fun `queryAs returns null for type mismatch for non nullable types`() {
        assertNull(
            json.queryAs<Int?> { path("shelter.cats.0.name") }
        )

    }

    @Test
    fun `queryAs works with mapT`() {
        val result = json.queryAs<List<Int>> {
            path("shelter.cats.age").each {
                mapT<Int, Int> { it + 1 }
            }
        }
        assertEquals(listOf(3, 4, 2), result)
    }

    @Test
    fun `queryAs with complex types`() {
        val shelter = json.queryAs<Shelter> { path("shelter") }
        assertEquals("Happy Shelter", shelter.name)
    }

    @Serializable
    data class Shelter(val name: String)
}

