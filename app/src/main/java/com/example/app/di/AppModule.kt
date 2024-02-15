package com.example.app.di

import com.example.app.data.remote.ApiService
import com.example.app.data.remote.ApiServiceImpl
import com.example.app.data.remote.HttpRoutes
import com.example.app.data.repository.PokemonRepositoryImpl
import com.example.app.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi(client:HttpClient):ApiService{
        return ApiServiceImpl(client)
    }

    @Singleton
    @Provides
    fun provideClient():HttpClient{
        return HttpClient(Android){
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

    @Singleton
    @Provides
    fun provideRepository(
        api:ApiService
    ):PokemonRepository{
        return PokemonRepositoryImpl(api)
    }
}