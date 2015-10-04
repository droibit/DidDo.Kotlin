package com.droibit.diddo.views.adapters

import android.content.Context
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.droibit.diddo.R
import com.droibit.diddo.models.ActivityDate
import com.droibit.diddo.views.ActivityDateView

/**
 * 活動日をリストに表示するためのアダプタ。
 *
 * @auther kumagai
 */
public class ActivityDateAdapter(context: Context): ArrayAdapter<ActivityDate>(context, R.layout.list_item_detail, android.R.id.text1) {

    /** {@inheritDoc} */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null)
                        LayoutInflater.from(context).inflate(R.layout.list_item_detail, parent, false) as ActivityDateView
                   else
                        convertView as ActivityDateView

        val item = getItem(position)
        view.dateView.text = DateFormat.getDateFormat(context).format(item.date)
        view.memoView.text = if (!TextUtils.isEmpty(item.memo))
                                item.memo
                            else
                                context.getString(R.string.empty_text)
        return view
    }

    /**
     * 末尾の[ActivityDate]オブジェクトを取得する。
     */
    public fun getLastItem(): ActivityDate? {
        return if (isEmpty)
                    null
               else
                    getItem(count - 1)
    }
}
