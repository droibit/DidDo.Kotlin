package com.droibit.diddo.utils

import android.content.Context
import com.droibit.easycreator.commit
import com.droibit.easycreator.getDefaultSharedPreferences

/**
 * アプリの設定を読み書きするためのユーティリティクラス。
 *
 * @auther kumagai
 * @since 15/03/28
 */
public final class SettingsUtils private() {

    companion object {

        private val KEY_ORDER = "order"

        /**
         * アクティビティの並び順を保存する。
         */
        fun setOrder(context: Context, order: Int) = context.getDefaultSharedPreferences().commit { putInt(KEY_ORDER, order) }

        /**
         * アクティビティの並び順を取得する。
         */
        fun getOrder(context: Context): Int = context.getDefaultSharedPreferences().getInt(KEY_ORDER, 0)
    }
}
