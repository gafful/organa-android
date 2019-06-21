package com.mukaase.android.organa.util

import android.content.Context
import com.mukaase.android.organa.console.ViewModelFactory
import com.mukaase.android.organa.engine.Engine
import com.mukaase.android.organa.engine.JAudioTagger

object InjectorUtils {

    fun provideConsoleViewModel(ctx: Context) = ViewModelFactory(Engine(JAudioTagger()))
}