package com.weather.weathermvvmapp.di.modules

import androidx.lifecycle.ViewModel
import com.weather.weathermvvmapp.di.utils.ViewModelKey
import com.weather.weathermvvmapp.di.scopes.FragmentScope
import com.weather.weathermvvmapp.weather.model.DetailedWeatherViewModel
import com.weather.weathermvvmapp.weather.ui.detailed_weather.DetailedWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class DetailedWeatherModule {

    /**
     * Generates an [AndroidInjector] for the [DetailedWeatherFragment].
     */
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeDetailedWeatherFragment(): DetailedWeatherFragment

    @Binds
    @IntoMap
    @ViewModelKey(DetailedWeatherViewModel::class)
    abstract fun bindDetailedWeatherViewModel(viewModel: DetailedWeatherViewModel): ViewModel
}