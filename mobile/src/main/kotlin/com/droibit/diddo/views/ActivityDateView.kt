package com.droibit.diddo.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.droibit.diddo.extension.bindView

/**
 * アクティビティの活動日を表示するためのビュー
 *
 * @auther kumagai
 * @since 15/03/18
 */
public class ActivityDateView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    val dateView: TextView by bindView(android.R.id.text1)
    val memoView: TextView by bindView(android.R.id.text2)
}
