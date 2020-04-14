package com.android.itunes.dagger.module

import androidx.lifecycle.ViewModel
import com.android.itunes.dagger.ViewModelKey
import com.android.itunes.ui.ItunesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ItunesViewModel::class)
    abstract fun bindItunesViewModel(viewModel: ItunesViewModel): ViewModel
}