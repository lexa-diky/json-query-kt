# Kotlin JSON Query 🚀

[![Kotlin](https://img.shields.io/badge/kotlin-2.1.20-blue.svg)](https://kotlinlang.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Build](https://img.shields.io/github/actions/workflow/status/lexa-diky/json-query-kt/gradle.yml?branch=main)](https://github.com/lexa-diky/json-query-kt/actions)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.lexa-diky/json-query)

A lightweight Kotlin library for querying and transforming JSON data using a fluent, composable API. ✨

## Features 🛠️

- 🧭 Traverse nested paths, index and slice JSON arrays
- 🔎 Select and filter JSON object properties
- 📊 Aggregate data common functions
- 🧩 Compose queries fluently

## Get Started 🚀

```kotlin
implementation("io.github.lexa-diky:json-query:<LATEST>")
```

## Quick Examples 🚦

### [Kotlin Notebook](./example/notebook.ipynb)

### Extract a field

```kotlin
val json = Json.parseToJsonElement("""{"name": "Alice", "age": 25, "address": {"city": "Wonderland"}}""")
println(json.query { path("name") }) // JsonPrimitive("Alice")
println(json.query { path("address.city") }) // JsonPrimitive("Wonderland")
println(json.query { path("address", "city") }) // JsonPrimitive("Wonderland")
```

### Aggregate: min, max, average

```kotlin
val json = Json.parseToJsonElement("""[1, 2, 3, 4, 5]""")
println(json.query { min() }) // JsonPrimitive(1)
println(json.query { max() }) // JsonPrimitive(5)
println(json.query { average() }) // JsonPrimitive(3)
```

### Filter, map, flatten

```kotlin
val json = Json.parseToJsonElement("""{"numbers": [1, 2, 3, 4, 5]}""")
val evens =
    println(
        json.query {
            path("numbers")
                .filter { it.jsonPrimitive.int % 2 == 0 }
        }
    ) // JsonArray([2, 4])

val nested = Json.parseToJsonElement("""[[1,2],[3,4]]""")
println(nested.query { flatten() }) // JsonArray([1,2,3,4])
```

## API Overview 📚

- `path(vararg segments: String)`: Traverse object keys
- `select(vararg properties: String)`: Select object properties
- `filter(predicate: (JsonPrimitive) -> Boolean)`: Filter array elements
- `flatten()`: Flatten nested arrays
- `get(index: Int)`, `get(range: IntRange)`: Index or slice arrays
