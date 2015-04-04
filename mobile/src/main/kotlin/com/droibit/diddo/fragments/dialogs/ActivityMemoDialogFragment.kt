package com.droibit.diddo.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.droibit.diddo.R
import com.droibit.diddo.models.ActivityDate
import com.droibit.diddo.models.newActivityDate
import com.droibit.easycreator.alertDialog
import com.droibit.easycreator.compat.fragment
import java.util.Date

/**
 * アクティビティの活動日の作成及び編集をするためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/15
 */
public class ActivityMemoDialogFragment : DialogFragment() {

    companion object {
        private val TAG = javaClass<ActivityMemoDialogFragment>().getSimpleName()
        private val ARG_SRC = "src"
        private val sDummyCallbacks = object: Callbacks {
            override fun onActivityDateEnterd(activityDate: ActivityDate) {
            }
        }

        /**
         * 新しいインスタンスを作成する
         */
        fun newInstance(activityDate: ActivityDate?): ActivityMemoDialogFragment {
            return fragment { args ->
                if (activityDate != null) {
                    args.putSerializable(ARG_SRC, activityDate)
                }
            }
        }
    }

    /**
     * アクティビティ名が入力された時に呼ばれるコールバック
     */
    trait Callbacks {
        fun onActivityDateEnterd(activityDate: ActivityDate)
    }

    private var mMemoEdit: EditText? = null
    private var mCallbacks: Callbacks = sDummyCallbacks

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        if (getTargetFragment() is Callbacks) {
            mCallbacks = getTargetFragment() as Callbacks
        }
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(getActivity(), R.layout.dialog_activity_date, null)

        mMemoEdit = view.findViewById(R.id.edit_memo) as EditText

        val srcActivityDate = getArguments().getSerializable(ARG_SRC) as? ActivityDate
        mMemoEdit!!.setText(srcActivityDate?.memo)

        val titleRes = if (srcActivityDate != null) R.string.title_modify_activity_memo else R.string.title_new_activity_date
        val positiveRes = if (srcActivityDate != null) R.string.button_modify else R.string.button_create
        val dialog = alertDialog(getActivity()) {
                setTitle(titleRes)
                setView(view)
                setPositiveButton(positiveRes) { dialog, whitch -> onDialogOk() }
                setNegativeButton(android.R.string.cancel, null)
        }
        // ダイアログを表示した際にキーボード表示する。
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }

    private fun onDialogOk() {
        val srcActivityDate = getArguments().getSerializable(ARG_SRC) as? ActivityDate
        if (srcActivityDate != null) {
            srcActivityDate.memo = mMemoEdit!!.getText().toString()
            mCallbacks.onActivityDateEnterd(srcActivityDate)
            return
        }

        val newActivityDate = newActivityDate {
            memo = mMemoEdit!!.getText().toString()
            date = Date()
        }
        mCallbacks.onActivityDateEnterd(newActivityDate)
    }
}
