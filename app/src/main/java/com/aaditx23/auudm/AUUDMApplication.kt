package com.aaditx23.auudm

import android.app.Application
import com.aaditx23.auudm.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AUUDMApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AUUDMApplication)
            modules(appModule)
        }
    }

}
