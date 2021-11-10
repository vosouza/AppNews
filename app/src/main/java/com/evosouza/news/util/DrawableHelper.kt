package com.evosouza.news.util

import android.content.Context
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setError(context: Context, resId: Int?) {
    error = if (resId != null) context.getString(resId) else null
}