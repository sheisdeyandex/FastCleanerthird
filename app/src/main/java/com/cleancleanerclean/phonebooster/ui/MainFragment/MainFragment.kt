package com.cleancleanerclean.phonebooster.ui.MainFragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.MainFragmentBinding
import com.cleancleanerclean.phonebooster.ui.MainFragment.permission.DialogPermissionFragment
import com.cleancleanerclean.phonebooster.ui.MainFragment.permission.DialogStoragePermissionFragment
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity


class MainFragment : Fragment(){
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = MainFragment()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initClicks()
        setUsages()
    }
    private fun setUsages(){
        binding.tvUsedMemory.text = viewModel.devidedStorage
        binding.lpiProgressMemory.progress = viewModel.progress.toInt()
        binding.lpiProgressRam.progress = viewModel.updateRam(requireActivity())
        binding.tvUsedRam.text = viewModel.updateRamString(requireActivity())
        binding.tvPercentMemory.text = viewModel.progress.toString()+"%"
    }
    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun initClicks(){
        val dialogPermissionFragment = DialogPermissionFragment()
        val dialogStoragePermissionFragment = DialogStoragePermissionFragment()
        binding.mcvClean.setOnClickListener {
            if(checkPermission()){
                changeFragment(R.id.action_mainFragment_to_cleanFragment)
            }
            else{
                dialogStoragePermissionFragment.show(childFragmentManager,"")
            }
        }
        binding.mcvBoost.setOnClickListener {
            changeFragment(R.id.action_mainFragment_to_boostFragment)
        }
        binding.mbtnBoost.setOnClickListener {
            changeFragment(R.id.action_mainFragment_to_boostFragment)
        }
        binding.mcvCool.setOnClickListener {
            changeFragment(R.id.action_mainFragment_to_coolFragment)
        }
        binding.mcvBatterySaver.setOnClickListener {
            if (viewModel.checkSystemWritePermission(requireContext())){
                changeFragment(R.id.action_mainFragment_to_batterySaverFragment)
            }
            else{
                dialogPermissionFragment.show(childFragmentManager,"")
            }
        }
        binding.mcvFileManager.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_fileManagerFragment)
        }
        binding.mcvAppManager.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_appsManagerFragment)
        }
        binding.ivCircle.setOnClickListener {
            binding.mcvClean.performClick()
        }
    }
    private fun changeFragment(id:Int){
        (requireActivity() as MainActivity).navController.navigate(id)
    }
}