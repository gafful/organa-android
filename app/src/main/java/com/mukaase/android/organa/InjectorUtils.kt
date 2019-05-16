package com.mukaase.android.organa

import android.content.Context

object InjectorUtils {

    fun provideConsoleViewModel(ctx: Context) = ConsoleViewModelFactory(Engine(JAudioTagger()))
}