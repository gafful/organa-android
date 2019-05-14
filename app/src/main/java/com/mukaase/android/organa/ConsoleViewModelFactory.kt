package com.mukaase.android.organa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConsoleViewModelFactory: ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = DashboardViewModel() as T
}