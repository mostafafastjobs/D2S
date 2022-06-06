package com.example.nytarticlestask.di

import com.example.nytarticlestask.db.database.AppDatabase
import com.example.nytarticlestask.network.ArticlesNetwork
import com.example.nytarticlestask.useCases.NetworkConnectionUseCase
import com.example.nytarticlestask.useCases.NetworkUseCase
import com.example.nytarticlestask.useCases.impl.ArticlesUseCases
import com.example.nytarticlestask.useCases.impl.NetworkUseCaseImpl
import com.example.nytarticlestask.view.main.MainVM
import com.example.nytarticlestask.view.main.MainView
import com.example.nytarticlestask.view.main.MainViewImpl
import com.example.smartzonetestnewcode.useCases.impl.NetworkConnectionUseCaseImpl
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule: Module = module {
    viewModel { MainVM(get(),get(),get()) }
}

@JvmField
val useCaseModules: Module = module {
    factory<NetworkUseCase> { NetworkUseCaseImpl(get()) }
    factory<MainView> { MainViewImpl() }
    factory<NetworkConnectionUseCase> { NetworkConnectionUseCaseImpl() }
    factory { ArticlesUseCases(get()) }
}

@JvmField
val dbRepoModules: Module = module {
    factory { AppDatabase.getAppDatabase(get()).articlesDataDao() }
}

@JvmField
val newsApiModule = module {
    single {
        ArticlesNetwork.getArticleApi(
            interceptors = listOf(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ),
        )
    }
}