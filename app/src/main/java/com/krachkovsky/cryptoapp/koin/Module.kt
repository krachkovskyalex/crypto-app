package com.krachkovsky.cryptoapp.koin

import com.krachkovsky.cryptoapp.api.CoinRetrofit
import com.krachkovsky.cryptoapp.view_models.CoinDetailsFragmentViewModel
import com.krachkovsky.cryptoapp.view_models.CryptoListFragmentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { CoinRetrofit() }
    viewModel { CryptoListFragmentViewModel(get(), get()) }
    viewModel { CoinDetailsFragmentViewModel(get(), get()) }
}