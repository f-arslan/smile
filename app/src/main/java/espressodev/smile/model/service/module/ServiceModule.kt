package espressodev.smile.model.service.module

import espressodev.smile.model.service.AccountService
import espressodev.smile.model.service.LogService
import espressodev.smile.model.service.StorageService
import espressodev.smile.model.service.impl.AccountServiceImpl
import espressodev.smile.model.service.impl.LogServiceImpl
import espressodev.smile.model.service.impl.StorageServiceImpl
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