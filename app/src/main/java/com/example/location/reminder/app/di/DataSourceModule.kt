package com.example.location.reminder.app.di

import com.example.location.reminder.authentication.data.source.firebase.AuthDataSource
import com.example.location.reminder.reminders.data.ReminderDataSource
import com.example.location.reminder.reminders.data.local.RemindersLocalRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    single { RemindersLocalRepository(get()) as ReminderDataSource }
    singleOf(::AuthDataSource)
}