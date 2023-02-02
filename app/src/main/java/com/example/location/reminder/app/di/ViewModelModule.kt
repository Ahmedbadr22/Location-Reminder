package com.example.location.reminder.app.di

import com.example.location.reminder.authentication.ui.viewModel.AuthViewModel
import com.example.location.reminder.reminders.data.ReminderDataSource
import com.example.location.reminder.reminders.reminderslist.RemindersListViewModel
import com.example.location.reminder.reminders.savereminder.SaveReminderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        RemindersListViewModel(
            get(),
            get() as ReminderDataSource
        )
    }
    //Declare singleton definitions to be later injected using by inject()
    single {
        //This view model is declared singleton to be used across multiple fragments
        SaveReminderViewModel(
            get(),
            get() as ReminderDataSource
        )
    }

    viewModelOf(::AuthViewModel)
}