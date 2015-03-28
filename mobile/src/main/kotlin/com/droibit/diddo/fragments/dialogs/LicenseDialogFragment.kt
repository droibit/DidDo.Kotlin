package com.droibit.diddo.fragments.dialogs

import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import com.droibit.easycreator.fragment
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.NoticesHtmlBuilder
import de.psdev.licensesdialog.NoticesXmlParser

/**
 * オープンソースライセンスを表示するためのダイアログ
 *
 * @auther kumagai
 * @since 15/03/28
 */
public class LicenseDialogFragment: DialogFragment() {

    companion object {

        private val ARG_NOTICES = "notices"

        /**
         * 新しいインスタンスを作成する
         */
        fun newInstance(noticesId: Int): LicenseDialogFragment {
            return fragment { args ->
                args.putInt(ARG_NOTICES, noticesId)
            }
        }

    }

    /** {@inheritDoc} */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val noticesId = getArguments().getInt(ARG_NOTICES)
        val dialog = LicensesDialog.Builder(getActivity())
                                   .setNotices(noticesId)
                                   .setShowFullLicenseText(true)
                                   .setCloseText(android.R.string.ok)
                                   .build()
                                   .create();
        dialog.setOnDismissListener(null)

        return dialog
    }
}