package com.example.project_simplrepair.hilt.module

import android.content.Context
import androidx.room.Room
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.DB.CustomerDAO
import com.example.project_simplrepair.DB.DeviceDAO
import com.example.project_simplrepair.DB.DevicePhotoDAO
import com.example.project_simplrepair.DB.RepairDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext ctx: Context
    ): AppDatabase = Room.databaseBuilder(
        ctx,
        AppDatabase::class.java,
        "Simpl Database"
    )
        .build()

    @Provides
    fun provideRepairDao(db: AppDatabase): RepairDAO {
        return db.repairDAO()
    }

    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDAO {
        return db.customerDao()
    }

    @Provides
    fun provideDeviceDao(db: AppDatabase): DeviceDAO {
        return db.deviceDao()
    }

    @Provides
    fun devicePhotos(db: AppDatabase): DevicePhotoDAO {
        return db.devicePhotoDao()
    }
}