package mysticfall.kotlin.react.test

import react.ReactElement

external interface TestRendererOptions {

    var createNodeMock: ((ReactElement) -> Any)?
}
