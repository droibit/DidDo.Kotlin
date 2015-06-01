package com.droibit.diddo.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.Toast
import com.droibit.diddo.R
import com.droibit.diddo.models.ActivityDate
import com.droibit.easycreator.alertDialog
import com.droibit.easycreator.compat.fragment
import com.droibit.easycreator.showToast
import com.squareup.timessquare.CalendarPickerView
import java.util.ArrayList
import java.util.Date
import java.util.NoSuchElementException
import java.util.concurrent.TimeUnit


/**
 * アクティビティの作成および編集をするためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class CalendarDialogFragment: DialogFragment() {

    companion object {
        private val ARG_DATES = "activity_dates"

        /**
         * 新しいインスタンスを作成する。
         */
        fun newInstance(activityDates: ArrayList<ActivityDate>): CalendarDialogFragment {
            return fragment { args ->
                args.putSerializable(ARG_DATES, activityDates)
            }
        }
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val calendar = View.inflate(getActivity(), R.layout.dialog_calendar, null) as CalendarPickerView

        // FIXME: 本来はソート済み
        var activityDates = getArguments().getSerializable(ARG_DATES) as List<ActivityDate>
        activityDates = activityDates.sortBy { it.date }

        // 活動日をハイライト表示する。
        val now = Date(System.currentTimeMillis())
        calendar.init(activityDates.first().date, now.nextDay())
                .withHighlightedDates(activityDates.map { it.date })

        // 選択されたセルが活動日の場合はトーストでメモを表示する。
        calendar.setCellClickInterceptor { date ->
            try {
                val hit = activityDates.first { it.date.equals(date) }
                if (hit.memo != null) {
                    showToast(getActivity(), hit.memo!!, Toast.LENGTH_SHORT)
                }
            } catch (e: NoSuchElementException) {
                e.printStackTrace()
            }
            true
        }

        return alertDialog(getActivity()) {
            setView(calendar)
        }
    }
}

private fun Date.nextDay(): Date {
    return Date(getTime() + TimeUnit.DAYS.toMillis(1))
}