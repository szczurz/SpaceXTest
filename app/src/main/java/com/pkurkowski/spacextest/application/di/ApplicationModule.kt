package com.pkurkowski.spacextest.application.di

import com.pkurkowski.spacextest.data.DataInterfaceImp
import com.pkurkowski.spacextest.domain.DataInterface
import com.pkurkowski.spacextest.presentation.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single<DataInterface> {
        DataInterfaceImp(
            get(), get(), get()
        )
    }
    viewModel { MainActivityViewModel(get(), get()) }
}