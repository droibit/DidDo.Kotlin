package com.droibit.diddo.fragments.dialogs

import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.app.Dialog
import android.app.AlertDialog
import com.droibit.diddo.R
import android.app.Activity
import com.droibit.easycreator.alertDialog
import com.droibit.easycreator.compat.fragment

/**
 * アクティビティをソートするためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/15
 */
public class SortActivityDialogFragment : DialogFragment() {

    companion  object {
        private val TAG = javaClass<SortActivityDialogFragment>().getSimpleName()
        private val ARG_POSITION = "position"
        private val sDummyCallbacks = object: Callbacks {
            override fun onSortChoiced(sort: Int) {
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
    trait Callbacks {
        fun onSortChoiced(sort: Int)
    }

    private var mCallbacks: Callbacks = sDummyCallbacks

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        if (getTargetFragment() is Callbacks) {
            mCallbacks = getTargetFragment() as Callbacks
        }
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val position = getArguments().getInt(ARG_POSITION)
        return alertDialog(getActivity()) {
                setSingleChoiceItems(R.array.sort_activity_labels, position) { (d, which) ->
                    mCallbacks.onSortChoiced(which)
                    dismiss()
                }
            }
    }
}
