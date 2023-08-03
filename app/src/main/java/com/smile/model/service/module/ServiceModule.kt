package com.smile.model.service.module

import com.smile.model.service.AccountService
import com.smile.model.service.LogService
import com.smile.model.service.StorageService
import com.smile.model.service.impl.AccountServiceImpl
import com.smile.model.service.impl.LogServiceImpl
import com.smile.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService
}