package com.droibit.diddo.views.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.droibit.diddo.R
import com.droibit.diddo.models.ActivityDate
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.droibit.diddo.views.ActivityDateView
import android.text.format.DateFormat
import android.text.TextUtils

/**
 * 活動日をリストに表示するためのアダプタ。
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class ActivityDateAdapter(context: Context): ArrayAdapter<ActivityDate>(context, R.layout.list_item_detail, android.R.id.text1) {

    /** {@inheritDoc} */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null)
                        LayoutInflater.from(getContext()).inflate(R.layout.list_item_detail, parent, false) as ActivityDateView
                   else
                        convertView as ActivityDateView

        val item = getItem(position)
        view.dateView.setText(DateFormat.getDateFormat(getContext()).format(item.date))
        view.memoView.setText(if (!TextUtils.isEmpty(item.memo))
                                    item.memo
                              else
                                    getContext().getString(R.string.empty_text)
                              )
        return view
    }

    /**
     * 末尾の[ActivityDate]オブジェクトを取得する。
     */
    public fun getLastItem(): ActivityDate? {
        return if (isEmpty())
                    null
               else
                    getItem(getCount()-1)
    }
}
