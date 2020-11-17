package com.tbright.avforandroid

import android.app.Application
import android.content.Context

class BaseApplication : Application() {
    companion object {
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
    }
}