package com.droibit.diddo.app

import com.activeandroid.app.Application
import com.activeandroid.ActiveAndroid

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