package com.droibit.diddo.fragments.dialogs

import android.support.v4.app.DialogFragment
import android.widget.Button
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.app.Activity
import android.os.Bundle
import android.app.Dialog
import android.app.AlertDialog
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.droibit.diddo.R
import android.view.View
import android.view.WindowManager
import android.content.DialogInterface
import com.droibit.diddo.models.UserActivity
import com.droibit.diddo.models.newUserActivity
import com.droibit.easycreator.alertDialog
import com.droibit.easycreator.compat.fragment

/**
 * アクティビティの作成および編集をするためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class ActivityDialogFragment: DialogFragment() {

    class object {
        private val TAG = javaClass<ActivityDialogFragment>().getSimpleName()
        /**
         * アクティビティ名が入力された時に呼ばれるコールバック
         */
        trait Callbacks {
            fun onActivityNameEnterd(activity: UserActivity)
        }

        private val ARG_SRC = "src"

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

    private var mPositiveButton: Button? = null
    private var mNameEdit: EditText? = null
    private var mCallbacks: Callbacks? = null

    private val mNameWather: TextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        // アクティビティ名が空の時はOK
        override fun afterTextChanged(s: Editable?) {
            mPositiveButton?.setEnabled(!TextUtils.isEmpty(s?.toString()))
        }
    }

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        mCallbacks = getTargetFragment() as? Callbacks
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(getActivity(), R.layout.dialog_activity, null)

        mNameEdit = view.findViewById(R.id.edit_activity) as EditText
        mNameEdit!!.addTextChangedListener(mNameWather)
        val containsSrc = getArguments().containsKey(ARG_SRC)
        if (containsSrc) {
            val srcActivity = getArguments().getSerializable(ARG_SRC) as UserActivity
            mNameEdit!!.setText(srcActivity.name)
            mNameEdit!!.selectAll();
        }

        val titleRes = if (containsSrc) R.string.title_activity_modify else R.string.title_new_activity
        val positiveRes = if (containsSrc) R.string.button_modify else R.string.button_create
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

    /** {@inheritDoc} */
    override fun onResume() {
        super.onResume()

        val dialog = getDialog() as AlertDialog
        mPositiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        mPositiveButton?.setEnabled(!TextUtils.isEmpty(mNameEdit!!.getText()))
    }

    private fun onDialogOk() {
        val srcActivity = getArguments().getSerializable(ARG_SRC) as? UserActivity
        if (srcActivity != null) {
            srcActivity.name = mNameEdit!!.getText().toString()
            mCallbacks?.onActivityNameEnterd(srcActivity)
            return
        }

        val newActivity = newUserActivity {
            name = mNameEdit!!.getText().toString()
        }
        mCallbacks?.onActivityNameEnterd(newActivity)

    }
}