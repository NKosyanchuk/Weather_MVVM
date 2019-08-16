package com.weather.weathermvvmapp.di.modules

import androidx.lifecycle.ViewModel
import com.weather.weathermvvmapp.di.utils.ViewModelKey
import com.weather.weathermvvmapp.di.scopes.FragmentScope
import com.weather.weathermvvmapp.weather.model.CurrentWeatherViewModel
import com.weather.weathermvvmapp.weather.ui.current_weather.CurrentWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class CurrentWeatherModule {

    /**
     * Generates an [AndroidInjector] for the [CurrentWeatherFragment].
     */
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeCurrentWeatherFragment(): CurrentWeatherFragment

    @Binds
    @IntoMap
    @ViewModelKey(CurrentWeatherViewModel::class)
    abstract fun bindCurrentWeatherViewModel(viewModel: CurrentWeatherViewModel): ViewModel
}