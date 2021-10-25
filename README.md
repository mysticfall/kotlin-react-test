# Kotlin API for React Test Renderer

Kotlin wrapper for [React Test Renderer](https://reactjs.org/docs/test-renderer.html), 
which can be used to unit test React components in a Kotlin/JS project.

## How to Use

### Installation

With Gradle (using Kotlin DSL):
```kotlin
implementation("io.github.mysticfall:kotlin-react-test:1.1.0")
```

Alternatively, using Groovy DSL:

```groovy
implementation "io.github.mysticfall:kotlin-react-test:1.1.0"
```

### Code Example

The most straightforward way of using the library is to make your test class implement 
`ReactTestSupport`, as shown below:

```kotlin
import mysticfall.kotlin.react.test.ReactTestSupport

class ComponentTest : ReactTestSupport {

    @Test
    fun testHeaderTitle() {
        val renderer = render {
            HeaderTitle {
                attrs {
                    title = "Kotlin/JS"
                }
            }
        }

        val title = renderer.root.findByType(HeaderTitle)

        assertEquals("Kotlin/JS", title.props.title)
    }
}
```

The project itself has quite an extensive set of test cases, which can serve as examples that show 
how various features of React Test Renderer can be used in Kotlin.

## LICENSE

This project is provided under the terms of [MIT License](LICENSE).
