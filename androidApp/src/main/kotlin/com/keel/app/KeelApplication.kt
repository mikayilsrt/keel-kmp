package com.keel.app

import android.app.Application
import com.keel.app.shell.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class KeelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@KeelApplication)
        }
    }
}
