package com.ktm.ktinder.presentation.di

import com.ktm.ktinder.data.network.ContactService
import com.ktm.ktinder.data.network.mock.MockContactService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindContactService(impl: MockContactService): ContactService
}