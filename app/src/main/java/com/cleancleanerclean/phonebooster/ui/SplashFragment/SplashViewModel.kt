package com.cleancleanerclean.phonebooster.ui.SplashFragment

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.cleancleanerclean.phonebooster.R


class SplashViewModel : ViewModel() {
    fun underlinePolicyText(string: String):SpannableString{
        val content = SpannableString(string)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        return content
    }
}