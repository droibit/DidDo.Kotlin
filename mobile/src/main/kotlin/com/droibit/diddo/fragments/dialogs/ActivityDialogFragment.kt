package com.droibit.diddo.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog;
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.droibit.diddo.R
import com.droibit.diddo.models.UserActivity
import com.droibit.diddo.models.newUserActivity
import com.droibit.easycreator.compat.fragment

/**
 * アクティビティの作成および編集をするためのダイアログ
 *
 * @auther kumagai
 */
public class ActivityDialogFragment: DialogFragment() {

    companion object {
        const private val ARG_SRC = "src"
        private val sDummyCallbacks = object: Callbacks {
            override fun onActivityNameEntered(activity: UserActivity) {
            }
        }

        /**
         * 新しいインスタンスを作成する
         */
        fun newInstance(activity: UserActivity?): ActivityDialogFragment {
            return fragment { args ->
                if (activity != null) {
                    args.putSerializable(ARG_SRC, activity)
                }
            }
        }
    }

    /**
     * アクティビティ名が入力された時に呼ばれるコールバック
     */
    interface Callbacks {
        fun onActivityNameEntered(activity: UserActivity)
    }

    private var mPositiveButton: Button? = null
    private var mNameEdit: EditText? = null
    private var mCallbacks: Callbacks = sDummyCallbacks

    private val mNameWatcher: TextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        // アクティビティ名が空の時はOK
        override fun afterTextChanged(s: Editable?) {
            mPositiveButton?.isEnabled = !TextUtils.isEmpty(s?.toString())
        }
    }

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        if (targetFragment is Callbacks) {
            mCallbacks = targetFragment as Callbacks
        }
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(context, R.layout.dialog_activity, null)

        mNameEdit = view.findViewById(R.id.edit_activity) as EditText
        mNameEdit!!.addTextChangedListener(mNameWatcher)
        val containsSrc = arguments.containsKey(ARG_SRC)
        if (containsSrc) {
            val srcActivity = arguments.getSerializable(ARG_SRC) as UserActivity
            mNameEdit!!.setText(srcActivity.name)
            mNameEdit!!.selectAll();
        }

        val titleRes = if (containsSrc) R.string.title_activity_modify else R.string.title_new_activity
        val positiveRes = if (containsSrc) R.string.button_modify else R.string.button_create
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

    /** {@inheritDoc} */
    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        mPositiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        mPositiveButton?.isEnabled = !TextUtils.isEmpty(mNameEdit!!.text)
    }

    private fun onDialogOk() {
        val srcActivity = arguments.getSerializable(ARG_SRC) as? UserActivity
        if (srcActivity != null) {
            srcActivity.name = mNameEdit!!.text.toString()
            mCallbacks.onActivityNameEntered(srcActivity)
            return
        }

        val newActivity = newUserActivity {
            name = mNameEdit!!.text.toString()
        }
        mCallbacks.onActivityNameEntered(newActivity)

    }
}