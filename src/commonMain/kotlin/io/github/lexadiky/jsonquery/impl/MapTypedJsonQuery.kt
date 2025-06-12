package io.github.lexadiky.jsonquery.impl

import io.github.lexadiky.jsonquery.JsonQuery
import io.github.lexadiky.jsonquery.util.asTyped
import io.github.lexadiky.jsonquery.util.asTypedBack
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlin.reflect.KType

@PublishedApi
internal class MapTypedJsonQuery<F, T>(
    private val inType: KType,
    private val outType: KType,
    private val transform: (F) -> T
) : JsonQuery {

    override fun select(json: JsonElement): JsonElement {
        val convertedType = json.asTyped<F>(inType) ?: return JsonNull
        return transform(convertedType).asTypedBack(outType) ?: JsonNull
    }

    override fun toString(): String {
        return "map($inType)"
    }
}
