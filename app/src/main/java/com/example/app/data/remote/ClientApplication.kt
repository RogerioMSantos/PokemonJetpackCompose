package com.example.app.data.remote

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@HiltAndroidApp
class ClientApplication:Application() {
    val client = HttpClient(Android){
        install(Logging){
            level = LogLevel.ALL
        }
        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }
}