package com.aaditx23.auudm.di

import androidx.room.Room
import com.aaditx23.auudm.data.NetworkMonitor
import com.aaditx23.auudm.data.local.database.AppDatabase
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.data.remote.datasource.FirestoreDataSource
import com.aaditx23.auudm.data.repository.ReceiptRepositoryImpl
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import com.aaditx23.auudm.domain.usecase.GetReceiptByIdUseCase
import com.aaditx23.auudm.domain.usecase.GetReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SaveReceiptUseCase
import com.aaditx23.auudm.domain.usecase.SearchReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SyncReceiptToFirestoreUseCase
import com.aaditx23.auudm.domain.usecase.SyncAllReceiptsToFirestoreUseCase
import com.aaditx23.auudm.domain.usecase.GetReceiptsFromFirestoreUseCase
import com.aaditx23.auudm.domain.usecase.SyncPendingReceiptsUseCase
import com.aaditx23.auudm.presentation.screens.AddReceiptScreen.AddReceiptViewModel

import com.aaditx23.auudm.presentation.screens.ListReceiptScreen.ReceiptListViewModel
import com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.ReceiptDetailsViewModel
import com.aaditx23.auudm.presentation.screens.SettingsScreen.SettingsViewModel
import com.google.firebase.firestore.FirebaseFirestore


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

    // Firestore
    single { FirebaseFirestore.getInstance() }
    single { FirestoreDataSource(get()) }

    // Repository
    single<ReceiptRepository> { ReceiptRepositoryImpl(get(), get()) }

    // UseCases
    factoryOf(::SaveReceiptUseCase)
    factoryOf(::GetReceiptsUseCase)
    factoryOf(::GetReceiptByIdUseCase)
    factoryOf(::SearchReceiptsUseCase)
    factoryOf(::SyncReceiptToFirestoreUseCase)
    factoryOf(::SyncAllReceiptsToFirestoreUseCase)
    factoryOf(::GetReceiptsFromFirestoreUseCase)
    factoryOf(::SyncPendingReceiptsUseCase)

    // Settings
    single { SettingsDataStore(androidContext()) }

    // Network Monitor
    single { NetworkMonitor(androidContext()) }

    // ViewModel
    viewModelOf(::AddReceiptViewModel)
    viewModelOf(::ReceiptListViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ReceiptDetailsViewModel)
}
