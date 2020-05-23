package com.codingwithset.minie_commerce

import android.app.Application
import com.facebook.stetho.Stetho

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        /*
        init stetho lib to be able to test sqlite database in chrome
        using this command in chrome browser [chrome://inspect]
         */
        Stetho.initializeWithDefaults(this)
    }
}