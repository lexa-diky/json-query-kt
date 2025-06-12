package io.github.lexadiky.jsonquery.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withoutInternalModifier
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withoutOverrideModifier
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withoutPrivateModifier
import com.lemonappdev.konsist.api.verify.assertTrue
import kotlin.test.Test

class DocumentationTest {

    @Test
    fun `all public apis should be documented`() {
        Konsist.scopeFromProject(sourceSetName = "commonMain")
            .classesAndInterfacesAndObjects()
            .withoutPrivateModifier()
            .withoutInternalModifier()
            .assertTrue { it.hasKDoc }

        Konsist.scopeFromProject(sourceSetName = "commonMain")
            .properties()
            .withoutPrivateModifier()
            .withoutInternalModifier()
            .withoutOverrideModifier()
            .assertTrue { it.hasKDoc }

        Konsist.scopeFromProject(sourceSetName = "commonMain")
            .functions()
            .withoutPrivateModifier()
            .withoutInternalModifier()
            .withoutOverrideModifier()
            .assertTrue { it.hasKDoc }
    }
}
