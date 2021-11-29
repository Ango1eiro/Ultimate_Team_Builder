package com.example.anitultimateteambuilder

import android.app.Application
import android.content.Context
import android.database.CursorWindow
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        appContext = applicationContext

    }

    companion object {
        lateinit var appContext: Context
    }



}