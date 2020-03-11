package ru.spbgororient.cityorientation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network

class App : Application() {
    lateinit var dataController: DataController

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        dataController = DataController(getSharedPreferences("settings", Context.MODE_PRIVATE), Network())
    }
}
