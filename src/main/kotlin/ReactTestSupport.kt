@file:Suppress("unused")

package mysticfall.kotlin.react.test

import kotlinext.js.jso
import react.RBuilder
import react.RBuilderSingle
import react.ReactElement

interface ReactTestSupport {

    fun ReactTestSupport.render(block: RBuilder.() -> Unit): TestRenderer = render(null, block)

    fun ReactTestSupport.render(
        options: TestRendererOptions? = null,
        block: RBuilder.() -> Unit
    ): TestRenderer {
        val builder = RBuilderSingle()

        block(builder)

        return TestRendererGlobal.create(builder.childList.first(), options).unsafeCast<TestRenderer>()
    }

    fun ReactTestSupport.render(
        mockFactory: (ReactElement) -> Any,
        block: RBuilder.() -> Unit
    ): TestRenderer {
        val options = jso<TestRendererOptions> {
            createNodeMock = mockFactory
        }

        return render(options, block)
    }

    fun ReactTestSupport.act(block: () -> Unit): Unit = TestRendererGlobal.act(block)

    fun ReactTestSupport.update(component: TestRenderer, replacement: RBuilder.() -> Unit) {
        val builder = RBuilderSingle()

        replacement(builder)

        component.update(builder.childList.first())
    }
}
