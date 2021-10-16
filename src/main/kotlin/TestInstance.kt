@file:Suppress("unused")

package mysticfall.kotlin.react.test

import react.Component
import react.ComponentType
import react.Props
import kotlin.js.Json


external interface TestInstance<T : Props> {

    var type: ComponentType<T>

    var props: T

    var parent: TestInstance<*>?

    var children: Array<TestInstance<*>>

    var instance: Component<*, *>?

    fun find(predicate: (TestInstance<*>) -> Boolean): TestInstance<*>

    fun findAll(predicate: (TestInstance<*>) -> Boolean): Array<TestInstance<*>>

    fun <P : Props> findByType(type: ComponentType<P>): TestInstance<P>

    fun findByType(type: String): TestInstance<*>?

    fun <P : Props> findAllByType(type: ComponentType<P>): Array<TestInstance<P>>

    fun findAllByType(type: String): Array<TestInstance<*>>

    fun <P : Props> findByProps(props: P): TestInstance<P>

    fun findByProps(props: Json): TestInstance<*>

    fun <P : Props> findAllByProps(props: P): Array<TestInstance<P>>

    fun findAllByProps(props: Json): Array<TestInstance<*>>
}
