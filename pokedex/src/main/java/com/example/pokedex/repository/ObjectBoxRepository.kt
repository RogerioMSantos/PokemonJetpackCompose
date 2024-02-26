package com.example.pokedex.repository

import android.content.Context
import com.example.pokedex.entities.MyObjectBox
import io.objectbox.BoxStore

object ObjectBoxRepository{
    lateinit var store: BoxStore

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}