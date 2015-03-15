package com.droibit.diddo.fragments.dialogs

import android.support.v4.app.DialogFragment
import android.os.Bundle
import com.droibit.diddo.models.ActivityDate
import android.widget.Button
import android.widget.EditText
import android.app.Activity
import android.app.Dialog
import android.view.View
import com.droibit.diddo.R
import android.app.AlertDialog
import android.view.WindowManager
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.droibit.diddo.models.newActivityDate
import java.util.Date

/**
 * アクティビティの活動日の作成及び編集をするためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/15
 */
public class ActivityDateDialogFragment: DialogFragment() {
    class object {
        private val TAG = javaClass<ActivityDateDialogFragment>().getSimpleName()
        private val ARG_SRC = "src"

        /**
         * アクティビティ名が入力された時に呼ばれるコールバック
         */
        trait Callbacks {

            /**
             * 活動日が入力/編集された時に呼ばれる処理
             */
            fun onActivityDateEnterd(activityDate: ActivityDate)
        }

        /**
         * 新しいインスタンスを作成する
         */
        fun newInstance(activityDate: ActivityDate?): ActivityDateDialogFragment {
            val args = Bundle(1)
            if (activityDate != null) {
                args.putSerializable(ARG_SRC, activityDate)
            }

            val f = ActivityDateDialogFragment()
            f.setArguments(args)
            return f
        }
    }

    private var mMemoEdit: EditText? = null
    private var mCallbacks: Callbacks? = null

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        mCallbacks = getTargetFragment() as? Callbacks
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(getActivity(), R.layout.dialog_activity_date, null)

        mMemoEdit = view.findViewById(R.id.edit_memo) as EditText

        val containsSrc = getArguments().containsKey(ARG_SRC)
        if (containsSrc) {
            mMemoEdit!!.setText(getArguments().getString(ARG_SRC))
        }

        val titleRes = if (containsSrc) R.string.title_modify_activity_memo else R.string.title_new_activity_date
        val positiveRes = if (containsSrc) R.string.button_modify else R.string.button_create
        val dialog = AlertDialog.Builder(getActivity())
                .setTitle(titleRes)
                .setView(view)
                .setPositiveButton(positiveRes) { (dialog, whitch) ->
                    onDialogOk()
                }.setNegativeButton(android.R.string.cancel, null)
                .create()

        // ダイアログを表示した際にキーボード表示する。
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }

    private fun onDialogOk() {
        val srcActivityDate = getArguments().getSerializable(ARG_SRC) as? ActivityDate
        if (srcActivityDate != null) {
            srcActivityDate.memo = mMemoEdit!!.getText().toString()
            mCallbacks?.onActivityDateEnterd(srcActivityDate)
            return
        }

        val newActivityDate = newActivityDate {
            memo = mMemoEdit!!.getText().toString()
            date = Date()
        }
        mCallbacks?.onActivityDateEnterd(newActivityDate)
    }

    // フラグメントを表示するためのヘルパーメソッド
    public fun show(srcFragment: Fragment) {
        setTargetFragment(srcFragment, 0)
        show(srcFragment.getFragmentManager(), TAG)
    }
}
