package com.evosouza.news.util

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.evosouza.news.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MessageDialog(
    private val title: String,
    private val message: String,
) : DialogFragment() {

    private var yesListener: (() -> Unit)? = null
    fun setYesListener(listener: () -> Unit) {
        yesListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton( getString(R.string.yes)) { _, _ ->
                yesListener?.let { yes ->
                    yes()
                }
            }
            .setNegativeButton(getString(R.string.no)) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()

}