package com.arctouch.codechallenge

import android.app.Application
import com.arctouch.codechallenge.module.appModule
import com.arctouch.codechallenge.util.Prefs
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

val prefs: Prefs by lazy {
    BaseApplication.prefs!!
}

class BaseApplication: Application() {
    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)

        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }
}