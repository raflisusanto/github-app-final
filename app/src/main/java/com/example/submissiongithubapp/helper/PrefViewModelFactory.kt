package com.example.submissiongithubapp.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissiongithubapp.SettingPreferences
import com.example.submissiongithubapp.viewmodel.FavoriteUserViewModel
import com.example.submissiongithubapp.viewmodel.SettingsViewModel
import com.example.submissiongithubapp.viewmodel.UserDetailViewModel

class PrefViewModelFactory private constructor(
    private val pref: SettingPreferences,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: PrefViewModelFactory? = null

        @JvmStatic
        fun getInstance(pref: SettingPreferences): PrefViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PrefViewModelFactory::class.java) {
                    INSTANCE = PrefViewModelFactory(pref)
                }
            }
            return INSTANCE as PrefViewModelFactory
        }
    }
}
