package com.example.datastoreplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.coroutineScope
import com.example.datastoreplayground.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var appSetting: AppSetting

    private var jobLanguage: Job? = null
    private var jobNotification: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appSetting = AppSetting(this)

        //radio button language
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, chakedId ->
           lifecycle.coroutineScope.launch(Dispatchers.IO) {
                if (chakedId == R.id.rb_engilsh){
                    appSetting.storeLanguageSetting("EN")
                } else {
                    appSetting.storeLanguageSetting("ID")
                }
            }

        }

        //switch notification
        binding.switchNotification.setOnCheckedChangeListener { _ , isChecked ->
            lifecycle.coroutineScope.launch {
                appSetting.storeNotificationSetting(isChecked)
            }
        }

        binding.switchDarkmode.setOnCheckedChangeListener { _ , isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        jobLanguage?.cancel()
        jobLanguage = lifecycle.coroutineScope.launchWhenCreated {
            appSetting.getLanguageSetting().collect { languge ->
                if (languge.equals("ID")){
                    binding.rbIndonesia.isChecked = true
                } else {
                    binding.rbEngilsh.isChecked = true
                }
            }
        }

        jobNotification?.cancel()
        jobNotification = lifecycle.coroutineScope.launchWhenCreated {
            appSetting.getNotificationSetting().collect {
                binding.switchNotification.isChecked = it
            }
        }


    }
}