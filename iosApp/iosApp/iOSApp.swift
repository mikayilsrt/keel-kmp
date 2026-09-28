import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        // Starts Koin once per process, before any Compose view is created.
        KoinIosKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
