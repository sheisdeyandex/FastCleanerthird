package com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.models

import android.graphics.drawable.Drawable

data class AppsModel (var packagename: String, var icon: Drawable,
                      var name: String,
                      var isCheckboxIsVisible: Boolean = false,
                      var isChecked: Boolean = false,
                      var id: Int = 0,
                      var size: String)


