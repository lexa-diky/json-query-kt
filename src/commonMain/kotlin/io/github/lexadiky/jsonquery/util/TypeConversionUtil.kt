@file:Suppress("UnsafeCallOnNullableType")

package io.github.lexadiky.jsonquery.util

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull
import kotlin.reflect.KType
import kotlin.reflect.typeOf

// Kotlin primitives
val TC_BYTE = typeOf<Byte>().classifier
val TC_SHORT = typeOf<Short>().classifier
val TC_INT = typeOf<Int>().classifier
val TC_LONG = typeOf<Long>().classifier
val TC_FLOAT = typeOf<Float>().classifier
val TC_DOUBLE = typeOf<Double>().classifier
val TC_BOOLEAN = typeOf<Boolean>().classifier
val TC_STRING = typeOf<String>().classifier

// JSON types
val TC_JSON_ELEMENT = typeOf<JsonElement>().classifier
val TC_JSON_OBJECT = typeOf<JsonObject>().classifier
val TC_JSON_PRIMITIVE = typeOf<JsonPrimitive>().classifier
val TC_JSON_ARRAY = typeOf<JsonArray>().classifier

val TC_JSON_TYPES_SET = setOf(
    TC_JSON_ELEMENT,
    TC_JSON_OBJECT,
    TC_JSON_PRIMITIVE,
    TC_JSON_ARRAY
)

// Collection types
val TC_LIST = typeOf<List<*>>().classifier
val TC_MAP = typeOf<Map<*, *>>().classifier

@Suppress("UNCHECKED_CAST", "ReturnCount", "CyclomaticComplexMethod")
internal fun <T> JsonElement.asTyped(type: KType): T? {
    if (this is JsonArray && type.classifier == TC_LIST) {
        val typeArgument = type.arguments.first().type ?: error("Cannot convert $this to type $type")
        return this.jsonArray.mapNotNull {
            it.asTyped(typeArgument)
        } as T
    }
    if (this is JsonObject && type.classifier == TC_MAP) {
        val typeArgument = type.arguments.last().type ?: error("Cannot convert $this to type $type")
        return this.jsonObject.mapValues { (_, v) ->
            val typed = v.asTyped<Any>(typeArgument)
            typed
        }.filterValues { it != null } as T
    }

    if (TC_JSON_TYPES_SET.contains(type.classifier)) {
        return this as T
    }

    if (this !is JsonPrimitive) return null


    return when (type.classifier) {
        TC_BYTE -> this.intOrNull?.toByte()
        TC_SHORT -> this.intOrNull?.toShort()
        TC_INT -> this.intOrNull
        TC_LONG -> this.longOrNull
        TC_FLOAT -> this.floatOrNull
        TC_DOUBLE -> this.floatOrNull?.toDouble()
        TC_BOOLEAN -> this.booleanOrNull
        TC_STRING -> this.contentOrNull
        else -> null
    } as T?
}

@Suppress("CyclomaticComplexMethod", "UNCHECKED_CAST", "ReturnCount")
internal fun <T> T.asTypedBack(type: KType): JsonElement? {
    if (this is List<*> && type.classifier == TC_LIST) {
        val typeArgument = type.arguments.first().type ?: error("Cannot convert $this to type $type")
        return JsonArray(
            (this as List<Any>).mapNotNull {
                it.asTypedBack(typeArgument)
            }
        )
    }

    if (this is Map<*, *> && type.classifier == TC_MAP) {
        val typeArgument = type.arguments.last().type ?: error("Cannot convert $this to type $type")
        return JsonObject(
            (this as Map<String, Any>).mapValues { (_, v) ->
                val typed = v.asTypedBack(typeArgument)
                typed ?: return null
            }
        )
    }

    return when (type.classifier) {
        TC_BYTE -> JsonPrimitive((this as? Byte) ?: this.toString().toByte())
        TC_SHORT -> JsonPrimitive((this as? Short) ?: this.toString().toShort())
        TC_INT -> JsonPrimitive((this as? Int) ?: this.toString().toInt())
        TC_LONG -> JsonPrimitive((this as? Long) ?: this.toString().toLong())
        TC_FLOAT -> JsonPrimitive((this as? Float) ?: this.toString().toFloat())
        TC_DOUBLE -> JsonPrimitive((this as? Double) ?: this.toString().toDouble())
        TC_BOOLEAN -> JsonPrimitive((this as? Boolean) ?: this.toString().toBoolean())
        TC_STRING -> JsonPrimitive((this as? String) ?: this.toString())
        else -> JsonPrimitive(this.toString())
    }
}
