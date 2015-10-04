package com.droibit.diddo.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.droibit.diddo.R
import com.droibit.diddo.models.ActivityDate
import com.droibit.diddo.models.newActivityDate
import com.droibit.easycreator.compat.fragment
import java.util.Date

/**
 * アクティビティの活動日の作成及び編集をするためのダイアログ
 *
 * @auther kumagai
 */
public class ActivityMemoDialogFragment : DialogFragment() {

    companion object {
        const private val ARG_SRC = "src"
        private val sDummyCallbacks = object: Callbacks {
            override fun onActivityDateEntered(activityDate: ActivityDate) {
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
    interface Callbacks {
        fun onActivityDateEntered(activityDate: ActivityDate)
    }

    private var mMemoEdit: EditText? = null
    private var mCallbacks: Callbacks = sDummyCallbacks

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        if (targetFragment is Callbacks) {
            mCallbacks = targetFragment as Callbacks
        }
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(context, R.layout.dialog_activity_date, null)

        mMemoEdit = view.findViewById(R.id.edit_memo) as EditText

        val srcActivityDate = arguments.getSerializable(ARG_SRC) as? ActivityDate
        mMemoEdit!!.setText(srcActivityDate?.memo)

        val titleRes = if (srcActivityDate != null) R.string.title_modify_activity_memo else R.string.title_new_activity_date
        val positiveRes = if (srcActivityDate != null) R.string.button_modify else R.string.button_create
        val dialog = AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                                .setTitle(titleRes)
                                .setView(view)
                                .setPositiveButton(positiveRes) { dialog, which -> onDialogOk() }
                                .setNegativeButton(android.R.string.cancel, null)
                                .create()

        // ダイアログを表示した際にキーボード表示する。
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }

    private fun onDialogOk() {
        val srcActivityDate = arguments.getSerializable(ARG_SRC) as? ActivityDate
        if (srcActivityDate != null) {
            srcActivityDate.memo = mMemoEdit!!.text.toString()
            mCallbacks.onActivityDateEntered(srcActivityDate)
            return
        }

        val newActivityDate = newActivityDate {
            memo = mMemoEdit!!.text.toString()
            date = Date()
        }
        mCallbacks.onActivityDateEntered(newActivityDate)
    }
}
