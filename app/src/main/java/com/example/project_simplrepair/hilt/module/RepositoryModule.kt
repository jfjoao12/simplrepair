// src/main/java/com/example/project_simplrepair/hilt/module/RepositoryModule.kt
package com.example.project_simplrepair.hilt.module

import com.example.project_simplrepair.hilt.TicketRepository
import com.example.project_simplrepair.hilt.TicketRepositoryImpl
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
    abstract fun bindTicketRepository(
        impl: TicketRepositoryImpl
    ): TicketRepository
}
