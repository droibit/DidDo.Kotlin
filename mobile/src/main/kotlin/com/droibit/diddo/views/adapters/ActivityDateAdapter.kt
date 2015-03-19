package com.droibit.diddo.views.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.droibit.diddo.R
import com.droibit.diddo.models.ActivityDate

/**
 * 活動日をリストに表示するためのアダプタ。
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class ActivityDateAdapter(context: Context): ArrayAdapter<ActivityDate>(context, R.layout.list_item_detail, android.R.id.text1) {

}