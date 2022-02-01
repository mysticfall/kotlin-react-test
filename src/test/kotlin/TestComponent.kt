package mysticfall.kotlin.react.test

import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.style

external interface TestProps : Props {
    var name: String
}

external interface TestState : State {
    var name: String
}

val TestFuncComponent = fc<TestProps> { props ->
    val (name, setName) = useState(props.name)

    useEffect(name) {
        setName("Updated: ${props.name}")
    }

    div {
        attrs.className = "test-component"
        h1 {
            attrs.className = "title"
            +name
        }
    }
}

class TestClassComponent(props: TestProps) : RComponent<TestProps, TestState>(props) {

    override fun TestState.init(props: TestProps) {
        name = props.name
    }

    override fun componentDidMount() {
        setState {
            name = "Updated: ${props.name}"
        }
    }

    override fun RBuilder.render() {
        div {
            attrs.className = "test-component"
            h1 {
                attrs.className = "title"
                +state.name
            }
        }
    }
}
