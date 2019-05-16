package com.mukaase.android.organa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConsoleViewModelFactory(val engine: Engine) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = DashboardViewModel(engine) as T
}