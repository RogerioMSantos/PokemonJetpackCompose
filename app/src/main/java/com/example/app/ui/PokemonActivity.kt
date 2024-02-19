package com.example.app.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.app.service.PokemonService


class PokemonActivity: AppCompatActivity() {

    private var mService: PokemonService? = null
    private val mBound = MutableLiveData(false)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            val binder = service as PokemonService.PokemonBinder
            mService = binder.getService()
            mBound.value = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound.value = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, PokemonService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}