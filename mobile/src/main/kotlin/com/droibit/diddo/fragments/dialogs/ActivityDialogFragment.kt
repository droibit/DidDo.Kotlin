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

/**
 * アクティビティの作成および編集をするためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/07
 */
public class ActivityDialogFragment: DialogFragment() {

    class object {
        val TAG = javaClass<ActivityDialogFragment>().getSimpleName()
        val ARG_NAME = "name"

        /**
         * アクティビティ名が入力された時に呼ばれるコールバック
         */
        trait Callbacks {

            /**
             * 新しいアクティビティ名が入力された時に呼ばれる処理
             */
            fun onActivityNameEnterd(activityName: String, isNew: Boolean)
        }

        /**
         * 新しいインスタンスを作成する
         */
        fun newInstance(activityName: String?): ActivityDialogFragment {
            val args = Bundle(1)
            if (activityName != null) {
                args.putString(ARG_NAME, activityName)
            }

            val f = ActivityDialogFragment()
            f.setArguments(args)
            return f
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
        val view = View.inflate(getActivity(), R.layout.dialog_new_item, null)

        mNameEdit = view.findViewById(R.id.edit_activity) as EditText

        val containsSrc = getArguments().containsKey(ARG_NAME)
        if (containsSrc) {
            mNameEdit!!.setText(getArguments().getString(ARG_NAME))
        }
        val titleRes = if (containsSrc) R.string.title_activity_modify else R.string.title_new_activity
        val positiveRes = if (containsSrc) R.string.button_modify else R.string.button_create

        val dialog = AlertDialog.Builder(getActivity())
                            .setTitle(titleRes)
                            .setView(view)
                            .setPositiveButton(positiveRes) { (dialog, whitch) ->
                                // 空文字の時はOKボタンを押せないのでコールバックするだけ。
                                mCallbacks?.onActivityNameEnterd(activityName = mNameEdit!!.getText().toString(),
                                                                 isNew        = !getArguments().containsKey(ARG_NAME))
                            }.setNegativeButton(android.R.string.cancel, null)
                            .create()

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

    // フラグメントを表示するためのヘルパーメソッド
    public fun show(srcFragment: Fragment) {
        setTargetFragment(srcFragment, 0)
        show(srcFragment.getFragmentManager(), TAG)
    }
}