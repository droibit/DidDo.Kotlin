package com.droibit.diddo.views.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.droibit.diddo.R

/**
 * アクティビティをリストに表示するためのアダプタ。
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class ActivityAdapter(context: Context): ArrayAdapter<String>(context, R.layout.list_item, android.R.id.text1) {
}