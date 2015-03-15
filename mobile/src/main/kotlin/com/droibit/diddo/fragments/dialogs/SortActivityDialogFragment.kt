package com.droibit.diddo.fragments.dialogs

import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.app.Dialog
import android.app.AlertDialog
import com.droibit.diddo.R
import android.app.Activity

/**
 * アクティビティをソートするためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/15
 */
public class SortActivityDialogFragment : DialogFragment() {

    class object {
        private val TAG = javaClass<SortActivityDialogFragment>().getSimpleName()
        private val ARG_POSITION = "position"

        /**
         * ソートの種類が選択された時に呼ばれるコールバック
         */
        trait Callbacks {
            fun onSortChoiced(sort: Int)
        }

        /**
         * 新しいインスタンスを作成する。
         */
        fun newInstance(position: Int): SortActivityDialogFragment {
            val args = Bundle(1)
            args.putInt(ARG_POSITION, position)

            val f = SortActivityDialogFragment()
            f.setArguments(args)
            return f
        }
    }

    private var mCallbacks: Callbacks? = null

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mCallbacks = getTargetFragment() as? Callbacks
    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val position = getArguments().getInt(ARG_POSITION)
        return AlertDialog.Builder(getActivity())
                    .setSingleChoiceItems(R.array.sort_activity_labels, position) { (d, which) ->
                        mCallbacks?.onSortChoiced(which)
                        dismiss()
                    }.create()
    }

    public fun show(srcFragment: Fragment) {
        setTargetFragment(srcFragment, 0)
        show(srcFragment.getFragmentManager(), TAG)
    }
}
