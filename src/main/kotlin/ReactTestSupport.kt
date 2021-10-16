@file:Suppress("unused")

package mysticfall.kotlin.react.test

import react.RBuilder
import react.RBuilderSingle

interface ReactTestSupport {

    fun ReactTestSupport.render(
        block: RBuilder.() -> Unit
    ): TestRenderer {
        val builder = RBuilderSingle()

        block(builder)

        return TestRendererGlobal.create(builder.childList.first()).unsafeCast<TestRenderer>()
    }

    fun ReactTestSupport.act(block: () -> Unit): Unit = TestRendererGlobal.act(block)

    fun ReactTestSupport.update(component: TestRenderer, replacement: RBuilder.() -> Unit) {
        val builder = RBuilderSingle()

        replacement(builder)

        component.update(builder.childList.first())
    }
}
