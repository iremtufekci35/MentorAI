package com.example.mentorai.di

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
Created by Irem TUFEKCI on 02/12/2025
 **/

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}