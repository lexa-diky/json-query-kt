@file:Suppress("MagicNumber")

package io.github.lexadiky.jsonquery

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Threads


@State(Scope.Benchmark)
@Threads(8)
@Fork(2)
class JsonQueryBenchmark {
    lateinit var element: JsonElement
    lateinit var json: Json

    @Setup(Level.Iteration)
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun setup() {
        val text = Thread.currentThread().contextClassLoader.getResource("dataset.json").readText()
        element = Json.parseToJsonElement(text)
        json = Json { ignoreUnknownKeys = true }
    }

    @Benchmark
    fun jsonQuery(blackhole: Blackhole) {
        blackhole.consume(
            element.query {
                path("shelter.cats.favoriteToys")
                    .flatten()
            }
        )
    }

    @Benchmark
    fun kotlinxSerializationShortMapping(blackhole: Blackhole) {
        blackhole.consume(
            json.decodeFromJsonElement<DatasetShort>(element)
                .shelter.cats
                .flatMap { it.favoriteToys }
        )
    }

    @Benchmark
    fun kotlinxSerializationFullMapping(blackhole: Blackhole) {
        blackhole.consume(
            json.decodeFromJsonElement<DatasetFull>(element)
                .shelter.cats
                .flatMap { it.favoriteToys }
        )
    }

    @Serializable
    data class DatasetShort(
        val shelter: Shelter
    ) {
        @Serializable
        data class Shelter(
            val cats: List<Cat>
        ) {
            @Serializable
            data class Cat(
                val favoriteToys: List<String>
            )
        }
    }

    @Serializable
    data class DatasetFull(
        val shelter: Shelter
    ) {
        @Serializable
        data class Shelter(
            val name: String,
            val location: Location,
            val cats: List<Cat>,
            val staff: List<Staff>,
            val events: List<Event>
        ) {
            @Serializable
            data class Location(
                val address: String,
                val city: String,
                val state: String,
                val zip: String
            )

            @Serializable
            data class Cat(
                val id: String,
                val name: String,
                val age: Int,
                val breed: String,
                val color: String,
                val gender: String,
                val adopted: Boolean,
                val owner: Owner? = null,
                val medicalRecords: List<MedicalRecord>,
                val favoriteToys: List<String>,
                val friends: List<String>
            ) {
                @Serializable
                data class Owner(
                    val name: String,
                    val contact: Contact,
                    val address: Address
                ) {
                    @Serializable
                    data class Contact(
                        val phone: String,
                        val email: String
                    )

                    @Serializable
                    data class Address(
                        val street: String,
                        val city: String,
                        val state: String
                    )
                }

                @Serializable
                data class MedicalRecord(
                    val date: String,
                    val type: String,
                    val details: String
                )
            }

            @Serializable
            data class Staff(
                val id: String,
                val name: String,
                val role: String
            )

            @Serializable
            data class Event(
                val name: String,
                val date: String,
                val description: String
            )
        }
    }
}
