@file:Suppress("unused")

package mysticfall.kotlin.react.test

import kotlinext.js.require
import react.Component
import react.ReactNode
import kotlin.js.Json

external interface TestRenderer {

    var root: TestInstance<*>

    fun toJSON(): Json

    fun toTree(): Json

    fun update(node: ReactNode)

    fun unmount()

    fun getInstance(): Component<*, *>?
}

//TODO Until we have https://youtrack.jetbrains.com/issue/KT-39602
object TestRendererGlobal {

    private val renderer = require("react-test-renderer")

    fun create(node: ReactNode): TestRenderer = renderer.create(node).unsafeCast<TestRenderer>()

    fun create(node: ReactNode, options: dynamic): TestRenderer =
        renderer.create(node, options).unsafeCast<TestRenderer>()

    fun act(block: () -> Any) {
        val callback: () -> Nothing? = {
            block()

            undefined
        }

        renderer.act(callback)
    }
}
