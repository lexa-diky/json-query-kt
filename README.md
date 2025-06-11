# Kotlin JSON Query 🚀

[![Kotlin](https://img.shields.io/badge/kotlin-2.1.20-blue.svg)](https://kotlinlang.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Build](https://img.shields.io/github/actions/workflow/status/lexa-diky/json-query-kt/gradle.yml?branch=main)](https://github.com/lexa-diky/json-query-kt/actions)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.lexa-diky/json-query)
A lightweight Kotlin library for querying and transforming JSON data using a fluent, composable API. ✨

## Features 🛠️

- 🔎 Select and filter JSON object properties
- 🧭 Traverse nested paths
- 🔢 Index and slice JSON arrays
- 🪄 Flatten nested arrays
- 🧪 Apply custom filters on JSON primitives
- 🧩 Compose queries fluently

## Usage 📦

Add the dependency to your project (see [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) for setup).

### Example 💡

```kotlin
import io.github.lexadiky.jsonquery.JsonQuery
import kotlinx.serialization.json.Json

fun main() {
    val element = Json.parseToJsonElement("""
        {
            "name": "John Doe",
            "age": 30,
            "isEmployed": true,
            "skills": ["Kotlin", "Java", "Python"],
            "projects": [
                {"name": "Project A", "status": "completed", "year": 2021, "tags": ["backend", "api", "ui"]},
                {"name": "Project B", "status": "in-progress", "year": 2022}
            ],
            "address": {
                "street": "123 Main St",
                "city": "Anytown",
                "zipCode": "12345"
            }
        }
    """.trimIndent())

    val query = JsonQuery {
        path("projects")
            .select("name", "tags")
            .path("tags")
            .flatten()
            .filter { it.content.contains("i") }
    }

    println(query.resolve(element))
}
```

## API Overview 📚

- `path(vararg segments: String)`: Traverse object keys
- `select(vararg properties: String)`: Select object properties
- `filter(predicate: (JsonPrimitive) -> Boolean)`: Filter array elements
- `flatten()`: Flatten nested arrays
- `get(index: Int)`, `get(range: IntRange)`: Index or slice arrays
