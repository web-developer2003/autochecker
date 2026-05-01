package com.autochecker.di

import com.autochecker.data.mock.MockSettingsRepository
import com.autochecker.data.repository.AuthRepository
import com.autochecker.data.repository.SettingsRepository
import com.autochecker.data.repository.UserRepository
import com.autochecker.data.repository.VehicleRepository
import com.autochecker.data.repository.impl.RealAuthRepository
import com.autochecker.data.repository.impl.RealUserRepository
import com.autochecker.data.repository.impl.RealVehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: RealAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(impl: RealVehicleRepository): VehicleRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: RealUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: MockSettingsRepository): SettingsRepository
}
