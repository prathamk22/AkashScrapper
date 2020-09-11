package com.example.akashscrapper.di

import android.app.Application
import androidx.room.Room
import com.example.akashscrapper.dashboard.DashboardRepository
import com.example.akashscrapper.dashboard.DashboardViewModel
import com.example.akashscrapper.database.AppDatabase
import com.example.akashscrapper.pdfActivity.PdfRepository
import com.example.akashscrapper.pdfActivity.PdfViewModel
import com.example.akashscrapper.utils.PreferenceHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { PdfViewModel(get()) }

    single { DashboardRepository(get(), get(), get()) }
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
}

