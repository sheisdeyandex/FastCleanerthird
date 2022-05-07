package com.cleancleanerclean.phonebooster.ui.MainFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.MainFragmentBinding
import com.cleancleanerclean.phonebooster.ui.MainFragment.permission.DialogPermissionFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainFragment : Fragment(){
    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
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
        CoroutineScope(Dispatchers.Main).let {

            binding.lpiProgressRam.progress = viewModel.updateRam(requireActivity())
            binding.tvUsedRam.text = viewModel.updateRamString(requireActivity())
        }
    }
    private fun initClicks(){
        val dialogPermissionFragment = DialogPermissionFragment()
        binding.mcvClean.setOnClickListener {
            changeFragment(R.id.action_mainFragment_to_cleanFragment)
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
    }
    private fun changeFragment(id:Int){
        (requireActivity() as MainActivity).navController.navigate(id)
    }
}