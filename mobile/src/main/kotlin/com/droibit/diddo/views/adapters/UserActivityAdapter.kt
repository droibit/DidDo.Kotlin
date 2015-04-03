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
import android.widget.ListView
import com.droibit.diddo.views.UserActivityView
import java.util.ArrayList

/**
 * アクティビティをリストに表示するためのアダプタ。
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class UserActivityAdapter(context: Context): ArrayAdapter<UserActivity>(context, R.layout.list_item, android.R.id.text1) {

    /** {@inheritDoc} */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null)
                        LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false) as UserActivityView
                   else
                        convertView as UserActivityView

        val item = getItem(position)
        view.elapsedView.setText(item.getElapsedDateFromNow(getContext()))
        view.nameView.setText(item.name)
        view.dateView.setText(DateFormat.getDateFormat(getContext()).format(item.recentlyDate))

        return view
    }

    public fun updateRow(listView: ListView, activity: UserActivity) {
        val position = getPosition(activity)
        val view = listView.getChildAt(position)

        // 詳細側でDBに保存してもリスト側には反映されないためここで更新する。
        getItem(position).recentlyDate = activity.recentlyDate
        getView(position, view, listView)
    }
}

public class ArrayAdapterIterator<T>(val adapter: ArrayAdapter<T>) {
    private var index = 0

    fun next() = adapter.getItem(index++)
    fun hasNext() = index < adapter.getCount()
}

public fun <T>ArrayAdapter<T>.iterator(): ArrayAdapterIterator<T> = ArrayAdapterIterator(this)

public fun <T>ArrayAdapter<T>.getItems(): List<T> {
    val items = ArrayList<T>(getCount())
    for (item in this) {
        items.add(item)
    }
    return items
}