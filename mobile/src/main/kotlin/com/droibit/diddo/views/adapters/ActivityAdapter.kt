package com.droibit.diddo.views.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.droibit.diddo.R
import com.droibit.diddo.models.UserActivity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import android.text.format.DateUtils
import android.text.format.DateFormat
import com.droibit.diddo.views.UserActivityView

/**
 * アクティビティをリストに表示するためのアダプタ。
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class ActivityAdapter(context: Context): ArrayAdapter<UserActivity>(context, R.layout.list_item, android.R.id.text1) {

    /** {@inheritDoc} */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null)
                        LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false) as UserActivityView
                    else
                        convertView as UserActivityView

        val item = getItem(position)
        view.nameView.setText(item.name)
        view.dateView.setText(DateFormat.getDateFormat(getContext()).format(item.recentlyDate))

        return view
    }
}