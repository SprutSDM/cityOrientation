package ru.spbgororient.cityorientation

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import ru.spbgororient.cityorientation.dataController.DataController

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DataController.initInstance(getSharedPreferences("settings", Context.MODE_PRIVATE))
    }
}
