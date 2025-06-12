package io.github.lexadiky.jsonquery.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withParentInterfaceNamed
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class JsonQueryImplementerTest {

    @Test
    fun `all JsonQuery implementations should be internal`() {
        Konsist.scopeFromProject()
            .classesAndInterfaces()
            .withParentInterfaceNamed("JsonQuery")
            .assertTrue { queryClass -> queryClass.hasInternalModifier }
    }

    @Test
    fun `all JsonQuery implementations names should be end with JsonQuery`() {
        Konsist.scopeFromProject()
            .classesAndInterfaces()
            .withParentInterfaceNamed("JsonQuery")
            .assertTrue { queryClass -> queryClass.name.endsWith("JsonQuery") }
    }

    @Test
    fun `all JsonQuery implementations names should be in impl package`() {
        Konsist.scopeFromProject()
            .classesAndInterfaces()
            .withParentInterfaceNamed("JsonQuery")
            .assertTrue { queryClass ->
                queryClass.packagee?.name == "io.github.lexadiky.jsonquery.impl"
            }
    }
}
