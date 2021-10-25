package mysticfall.kotlin.react.test

import org.w3c.dom.Element
import react.*
import react.dom.div
import kotlin.js.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestRendererTest : ReactTestSupport {

    private fun withComponents(block: (ComponentType<TestProps>, TestRenderer) -> Unit) {
        fun create(block: RBuilder.() -> Unit): TestRenderer {
            lateinit var result: TestRenderer

            act {
                result = render {
                    block()
                }
            }

            return result
        }
        block(TestFuncComponent, create {
            TestFuncComponent {
                attrs {
                    name = "Test"
                }
            }
        })

        block(TestClassComponent::class.react, create {
            child(TestClassComponent::class) {
                attrs {
                    name = "Test"
                }
            }
        })
    }

    @Test
    fun testRoot() = withComponents { type, component ->
        assertEquals(type, component.root.type, "Unexpected root type for $type")
    }

    @Test
    fun testToJSON() = withComponents { type, component ->
        val expected = """
          |{
          |  "type": "div",
          |  "props": {
          |     "className": "test-component",
          |     "style": {}
          |  },
          |  "children": [
          |     {
          |         "type": "h1",
          |         "props": {
          |             "className": "title",
          |             "style": {}
          |         },
          |         "children": [
          |             "Updated: Test"
          |         ]
          |     }
          |  ]
          |}
        """.trimMargin()

        assertEquals(
            JSON.stringify(JSON.parse(expected)),
            JSON.stringify(component.toJSON()),
            "Unexpected JSON contents for $type"
        )
    }

    @Test
    fun testToTree() = withComponents { type, component ->
        val actual = component.toTree().asDynamic()

        val message = "Unexpected return value of toTree() for $type"

        assertEquals("component", actual["nodeType"], message)
        assertEquals("host", actual["rendered"]["nodeType"], message)
        assertEquals("div", actual["rendered"]["type"], message)
        assertEquals("title", actual["rendered"]["rendered"][0]["props"]["className"], message)
    }

    @Test
    fun testUpdate() = withComponents { type, component ->
        act {
            update(component) {
                div(classes = "test") {}
            }
        }

        val actual = JSON.stringify(component.toJSON())
        val expected = """{"type":"div","props":{"className":"test","style":{}},"children":null}"""

        assertEquals(expected, actual, "Unexpected component state after update for $type")
    }

    @Test
    fun testUnmount() = withComponents { type, component ->
        component.unmount()

        assertNull(component.toJSON(), "toJSON() should return null for unmounted components ($type)")
    }

    @Test
    fun testGetInstance() = withComponents { type, component ->
        if (type == TestFuncComponent) {
            assertNull(component.getInstance(), "getInstance() should return null for function components")
        } else {
            assertTrue(component.getInstance() is TestClassComponent, "Unexpected type of getInstance()")
        }
    }

    @Test
    fun testCreateNodeMock() {
        @Suppress("LocalVariableName")
        val ComponentWithRef = fc<TestProps> {
            val elem = createRef<Element>()
            val (name, setName) = useState("initial")

            useEffect(name) {
                setName(elem.current?.className ?: "no ref")
            }

            div(classes = "test-component") {
                ref = elem

                +name
            }
        }

        fun mock(elem: ReactElement): Any {
            assertEquals("test-component", elem.props.asDynamic().className)

            return json(Pair("className", "test"))
        }

        lateinit var result: TestRenderer

        act {
            result = render(::mock) {
                ComponentWithRef {}
            }
        }

        val data = result.toJSON().asDynamic()

        assertEquals("test-component", data.props.className)
        assertEquals("test", data.children[0])
    }
}
