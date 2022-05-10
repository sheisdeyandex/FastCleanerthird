package com.cleancleanerclean.phonebooster.ui.MainFragment.permission

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.cleancleanerclean.phonebooster.databinding.DialogPermissionBinding

class DialogPermissionFragment : DialogFragment() {
    private fun openAndroidPermissionsMenu() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + requireActivity().packageName)
        startActivity(intent)
    }
    lateinit var binding: DialogPermissionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPermissionBinding.inflate(inflater, container, false)
        val v: View = binding.root
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(true)
        binding.mbAllow.setOnClickListener {
            openAndroidPermissionsMenu()
            dismiss()
        }
        return v
    }
}