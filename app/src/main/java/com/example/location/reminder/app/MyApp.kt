package com.example.location.reminder.app

import android.app.Application
import com.example.location.reminder.app.di.appModule
import com.example.location.reminder.app.di.dataSourceModule
import com.example.location.reminder.app.di.repositoryModule
import com.example.location.reminder.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    appModule,
                    dataSourceModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}