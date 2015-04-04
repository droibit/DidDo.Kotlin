package com.droibit.diddo.app

import com.activeandroid.ActiveAndroid
import com.activeandroid.app.Application

/**
 * ActiveAndroidを初期化するために使用する。
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class MyApplication: Application() {

    /** {@inheritDoc} */
    override fun onCreate() {
        super.onCreate()
        ActiveAndroid.initialize(this)
    }
}