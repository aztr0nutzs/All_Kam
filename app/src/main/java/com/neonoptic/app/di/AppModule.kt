package com.neonoptic.app.di

import android.content.Context
import com.neonoptic.app.network.OnvifClient
import com.neonoptic.app.usb.UsbCameraManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUsbCameraManager(@ApplicationContext context: Context): UsbCameraManager =
        UsbCameraManager(context)

    @Provides
    @Singleton
    fun provideOnvifClient(): OnvifClient = OnvifClient()
}
