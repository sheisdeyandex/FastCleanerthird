package com.cleancleanerclean.phonebooster.dry

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

object AnimChange {
    fun changeAnimToWork(animLayout:ConstraintLayout, workLayout:ConstraintLayout){
        animLayout.visibility = View.GONE
        workLayout.visibility= View.VISIBLE
    }
    fun changeWorkToAnim(animLayout:ConstraintLayout, workLayout:ConstraintLayout){
        workLayout.visibility= View.GONE
        animLayout.visibility = View.VISIBLE
    }
}