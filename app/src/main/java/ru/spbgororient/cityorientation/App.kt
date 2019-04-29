package ru.spbgororient.cityorientation

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import retrofit2.Retrofit
import ru.spbgororient.cityorientation.api.CityOrientationApi
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import kotlin.concurrent.timer

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DataController.initInstance(getSharedPreferences("settings", Context.MODE_PRIVATE))
    }
}