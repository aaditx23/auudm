package com.aaditx23.auudm.di

import androidx.room.Room
import com.aaditx23.auudm.data.local.database.AppDatabase
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.data.repository.ReceiptRepositoryImpl
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import com.aaditx23.auudm.domain.usecase.GetReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SaveReceiptUseCase
import com.aaditx23.auudm.domain.usecase.SearchReceiptsUseCase
import com.aaditx23.auudm.presentation.screens.AddReceiptScreen.AddReceiptViewModel
import com.aaditx23.auudm.presentation.screens.ListReceiptScreen.ListReceiptViewModel
import com.aaditx23.auudm.presentation.screens.SettingsScreen.SettingsViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "auudm.db"
        ).build()
    }

    // DAO
    single { get<AppDatabase>().receiptDao() }

    // Repository
    single<ReceiptRepository> { ReceiptRepositoryImpl(get()) }

    // UseCases
    factoryOf(::SaveReceiptUseCase)
    factoryOf(::GetReceiptsUseCase)
    factoryOf(::SearchReceiptsUseCase)

    // Settings
    single { SettingsDataStore(androidContext()) }

    // ViewModel
    viewModelOf(::AddReceiptViewModel)
    viewModelOf(::ListReceiptViewModel)
    viewModelOf(::SettingsViewModel)
}
