package com.example.datastoreplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.coroutineScope
import com.example.datastoreplayground.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var appSetting: AppSetting

//    private val switchMaterial by lazy {
//        findViewById<SwitchMaterial>(R.id.switch_darkmode)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.coroutineScope.launch {
            appSetting.getDarkmodeSetting().collectLatest {
                if(it){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



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
            lifecycle.coroutineScope.launch {
                appSetting.storeDarkmodeSetting(isChecked)
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

        }

        lifecycle.coroutineScope.launchWhenCreated {
            appSetting.getLanguageSetting().collect { languge ->
                if (languge.equals("ID")){
                    binding.rbIndonesia.isChecked = true
                } else {
                    binding.rbEngilsh.isChecked = true
                }
            }
        }

        lifecycle.coroutineScope.launchWhenCreated {
            appSetting.getNotificationSetting().collect {
                binding.switchNotification.isChecked = it
            }
        }

       lifecycle.coroutineScope.launchWhenCreated {
          appSetting.getDarkmodeSetting().collect {
              binding.switchDarkmode.isChecked = it
          }
       }
    }
}