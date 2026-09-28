package com.keel.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform