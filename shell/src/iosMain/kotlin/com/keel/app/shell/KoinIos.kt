package com.keel.app.shell

import com.keel.app.shell.di.initKoin

/** Called once from the SwiftUI `App` initializer (exported to Swift as `KoinIosKt.doInitKoinIos()`). */
fun initKoinIos() {
    initKoin()
}
