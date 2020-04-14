package com.android.itunes.dagger.module

import android.app.Application
import com.android.itunes.ItunesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        ActivityBindingModule::class]
)
interface ApplicationComponent : AndroidInjector<ItunesApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: Application)
}