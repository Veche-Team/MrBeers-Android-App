package com.example.neverpidor.presentation

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.data.settings.SharedPreferencesAppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val appSettings: AppSettings
): ViewModel(), OnSharedPreferenceChangeListener {

    private val _userName = MutableStateFlow(appSettings.getCurrentUser().name)
    val userName: StateFlow<String> = _userName

    init {
        addListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when (key) {
            SharedPreferencesAppSettings.PREF_CURRENT_USER_NUMBER -> {
                setName()
            }
        }
    }

    private fun addListener(listener: OnSharedPreferenceChangeListener) {
        appSettings.addListener(listener)
    }
    private fun setName() = viewModelScope.launch {
        _userName.emit(appSettings.getCurrentUser().name)
    }

    fun logout() {
        appSettings.setCurrentUser(UserEntity())
    }
}