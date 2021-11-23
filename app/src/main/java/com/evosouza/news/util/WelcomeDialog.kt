package com.evosouza.news.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.evosouza.news.R
import kotlinx.coroutines.*

class WelcomeDialog(
    private val buttonListener: () -> Unit
): DialogFragment() {

    private lateinit var image: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_welcome, container, false)
        val button: Button = view.findViewById(R.id.btnNextScreen)
        image = view.findViewById(R.id.imageLogo)
        button.setOnClickListener {
            buttonListener()
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            it.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Glide.with(requireContext()).load(R.drawable.giff_logotipo).into(image)
        CoroutineScope(Dispatchers.Main)
            .launch {
                delay(3000)
                image.setImageResource(R.drawable.logo_com_fundo)
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.create()

        return dialog
    }
}