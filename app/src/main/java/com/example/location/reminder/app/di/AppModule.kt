package com.example.location.reminder.app.di

import com.example.location.reminder.reminders.data.local.LocalDB
import org.koin.dsl.module

val appModule = module {
    single { LocalDB.createRemindersDao(get()) }
}