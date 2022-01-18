package com.krachkovsky.cryptoapp

import android.app.Application
import com.krachkovsky.cryptoapp.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CryptoApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@CryptoApp)
            modules(appModule)
        }
    }
}