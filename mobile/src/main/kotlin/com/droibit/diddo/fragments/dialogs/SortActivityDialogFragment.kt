package com.droibit.diddo.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.droibit.diddo.R
import com.droibit.easycreator.compat.fragment

/**
 * アクティビティをソートするためのダイアログ
 *
 * @auther kumagai
 */
public class SortActivityDialogFragment : DialogFragment() {

    companion  object {
        const private val ARG_POSITION = "position"

        private val sDummyCallbacks = object: Callbacks {
            override fun onSortChose(order: Int) {
            }
        }

        /**
         * 新しいインスタンスを作成する。
         */
        fun newInstance(position: Int): SortActivityDialogFragment {
            return fragment { args ->
                args.putInt(ARG_POSITION, position)
            }
        }
    }

    /**
     * ソートの種類が選択された時に呼ばれるコールバック
     */
    interface Callbacks {
        fun onSortChose(order: Int)
    }

    private var mCallbacks: Callbacks = sDummyCallbacks

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        if (targetFragment is Callbacks) {
            mCallbacks = targetFragment as Callbacks
        }
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val position = arguments.getInt(ARG_POSITION)
        return AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                          .setSingleChoiceItems(R.array.sort_activity_labels, position) { d, which ->
                                                    mCallbacks.onSortChose(which)
                                                    dismiss()
                                                }
                          .create()

    }
}
