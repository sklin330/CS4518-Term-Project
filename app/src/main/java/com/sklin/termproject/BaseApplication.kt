package com.sklin.termproject

import android.app.Application
import android.content.Context
import com.sklin.termproject.viewmodel.achievement.AchievementSource

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AchievementSource.initialize(this)
    }

    init {
        instance = this
    }

    companion object {
        private var instance: BaseApplication? = null

        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
    }
}