package io.github.lexadiky.jsonquery.util

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Suppress("UNCHECKED_CAST")
internal fun <T> JsonElement.asTyped(type: KType): T? {
    if (this !is JsonPrimitive) return null

    return when (type) {
        typeOf<Byte>() -> this.intOrNull?.toByte()
        typeOf<Short>() -> this.intOrNull?.toShort()
        typeOf<Int>() -> this.intOrNull
        typeOf<Long>() -> this.longOrNull
        typeOf<Float>() -> this.floatOrNull
        typeOf<Double>() -> this.float.toDouble()
        typeOf<Boolean>() -> this.booleanOrNull
        typeOf<String>() -> this.contentOrNull
        else -> null
    } as T?
}

internal fun <T> T.asTypedBack(type: KType): JsonPrimitive {
    return when (type) {
        typeOf<Byte>() -> JsonPrimitive((this as? Byte) ?: this.toString().toByte())
        typeOf<Short>() -> JsonPrimitive((this as? Short) ?: this.toString().toShort())
        typeOf<Int>() -> JsonPrimitive((this as? Int) ?: this.toString().toInt())
        typeOf<Long>() -> JsonPrimitive((this as? Long) ?: this.toString().toLong())
        typeOf<Float>() -> JsonPrimitive((this as? Float) ?: this.toString().toFloat())
        typeOf<Double>() -> JsonPrimitive((this as? Double) ?: this.toString().toDouble())
        typeOf<Boolean>() -> JsonPrimitive((this as? Boolean) ?: this.toString().toBoolean())
        typeOf<String>() -> JsonPrimitive((this as? String) ?: this.toString())
        else -> JsonPrimitive(this.toString())
    }
}
