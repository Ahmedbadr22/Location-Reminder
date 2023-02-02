package com.example.location.reminder.app.di

import com.example.location.reminder.authentication.data.repository.AuthRepository
import com.example.location.reminder.authentication.data.repository.AuthRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
}