package com.example.core.di

import android.app.Application
import androidx.room.Room
import com.example.core.dashboard.DashboardRepository
import com.example.core.dashboard.DashboardViewModel
import com.example.data.database.AppDatabase
import com.example.core.pdfActivity.PdfRepository
import com.example.core.pdfActivity.PdfViewModel
import com.example.core.utils.PreferenceHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { PdfViewModel(get()) }

    single { DashboardRepository(get()) }
    single { PdfRepository(get()) }
}

val preferencesModule = module {
    single { provideSettingsPreferences(androidApplication()) }
}

fun provideSettingsPreferences(app: Application): PreferenceHelper = PreferenceHelper.getPrefs(app)

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "akash-scrapper-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        val database: AppDatabase = get()
        database.getSemesterDao()
    }

    factory {
        val database: AppDatabase = get()
        database.subjectDao()
    }

    factory {
        val database: AppDatabase = get()
        database.filesDao()
    }

    factory {
        val database: AppDatabase = get()
        database.fileDataDao()
    }
}

