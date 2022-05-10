package com.cleancleanerclean.phonebooster.interfaces

import android.net.Uri

interface ICheckbox {
    fun checked(i: Int)
    fun notChecked(deleteProgress: Boolean)
    fun delete(integers: ArrayList<Int>, uris: ArrayList<Uri?>?, ids: ArrayList<Int>)
    fun share(Uris: Boolean)
}