package com.example.app.repository

import android.content.Context
import com.example.app.entities.MyObjectBox
import io.objectbox.BoxStore

object ObjectBoxRepository{
    lateinit var store: BoxStore

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}