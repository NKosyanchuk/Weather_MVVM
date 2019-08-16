package com.weather.weathermvvmapp.di.modules

import androidx.lifecycle.ViewModel
import com.weather.weathermvvmapp.di.utils.ViewModelKey
import com.weather.weathermvvmapp.di.scopes.FragmentScope
import com.weather.weathermvvmapp.weather.model.FutureWeatherViewModel
import com.weather.weathermvvmapp.weather.ui.future_weather.FutureWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class FutureWeatherModule {

    /**
     * Generates an [AndroidInjector] for the [FutureWeatherFragment].
     */
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeFuttureWeatherFragment(): FutureWeatherFragment

    @Binds
    @IntoMap
    @ViewModelKey(FutureWeatherViewModel::class)
    abstract fun bindAgendaViewModel(viewModel: FutureWeatherViewModel): ViewModel
}