package com.arctouch.codechallenge.module

import com.arctouch.codechallenge.ui.detail.DetailsViewModel
import com.arctouch.codechallenge.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { DetailsViewModel() }
    viewModel { HomeViewModel() }
}