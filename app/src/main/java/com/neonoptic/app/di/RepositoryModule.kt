package com.neonoptic.app.di

import com.neonoptic.app.data.repository.CameraRepositoryImpl
import com.neonoptic.app.domain.repository.CameraRepository
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
    abstract fun bindCameraRepository(impl: CameraRepositoryImpl): CameraRepository
}
