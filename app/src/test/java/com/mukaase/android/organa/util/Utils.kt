package com.mukaase.android.organa.util

import org.mockito.Mockito

object Utils {

    fun <T> anyObject(): T {
        return Mockito.any<T>()
    }

//    fun <T> anyObject(): T {
//        Mockito.any<T>()
//        return uninitialized()
//    }
//
//    private fun <T> uninitialized(): T = null as T
}