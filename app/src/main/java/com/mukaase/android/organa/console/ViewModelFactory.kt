package com.mukaase.android.organa.console

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mukaase.android.organa.engine.Engine

class ConsoleViewModelFactory(val engine: Engine) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = with(modelClass){
        when {
            isAssignableFrom(ConsoleViewModel::class.java) -> ConsoleViewModel(engine)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}