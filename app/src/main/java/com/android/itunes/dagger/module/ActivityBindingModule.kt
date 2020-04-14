package com.android.itunes.dagger.module

import com.android.itunes.dagger.ViewModelBuilder
import com.android.itunes.ui.AlbumDetailFragment
import com.android.itunes.ui.ListFragment
import com.android.itunes.ui.SearchActivity
import com.android.itunes.ui.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun searchActivity(): SearchActivity

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun searchFragment(): SearchFragment

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun listFragment(): ListFragment

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun albumDetailFragment(): AlbumDetailFragment
}