package com.droibit.diddo.views

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import com.droibit.diddo.R
import com.droibit.diddo.extension.bindView

/**
 * リストビューにアクティビティ情報を表示するためのビュー
 *
 * @auther kumagai
 * @since 15/03/18
 */

public class UserActivityView(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    val elapsedView: TextView by bindView(R.id.passed)
    val nameView: TextView by bindView(android.R.id.text1)
    val dateView: TextView by bindView(android.R.id.text2)
}
