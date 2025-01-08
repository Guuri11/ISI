package org.guuri11.isimultiplatform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BluetoothViewModel : ViewModel() {
    private val _isCarConnected = MutableLiveData<Boolean>()
    private val _saveCarCoordinates = MutableLiveData<Boolean>()
    val saveCarCoordinates: LiveData<Boolean> get() = _saveCarCoordinates

    fun updateConnectionStatus(isConnected: Boolean) {
        if (_isCarConnected.value == true && !isConnected) {
            _saveCarCoordinates.value = true
        } else {
            _saveCarCoordinates.value = false
        }
        _isCarConnected.value = isConnected
    }
}