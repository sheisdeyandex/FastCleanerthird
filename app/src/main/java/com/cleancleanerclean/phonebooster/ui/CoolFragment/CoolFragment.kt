package com.cleancleanerclean.phonebooster.ui.CoolFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleancleanerclean.phonebooster.dry.AnimChange
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.CoolFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoolFragment : Fragment() {
    private var _binding: CoolFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = CoolFragment()
    }

    private lateinit var viewModel: CoolViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoolFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setAnim(finish:Boolean){
       CoroutineScope(Dispatchers.Main).launch {
           delay(viewModel.timerTime)
           if(finish){
               (requireActivity() as MainActivity).navController.navigate(R.id.action_coolFragment_to_finishOrRecommend)

           }
           else{
               binding.tvFoundTemperature.text = viewModel.cpuTemperature().toString()
               AnimChange.changeAnimToWork(binding.clCoolScan,binding.clCool)
           }
       }



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CoolViewModel::class.java]

        binding.ivBoostFinished.setImageDrawable(viewModel.initImageDrawable(requireContext()))
        binding.mbtnCool.setOnClickListener {
            AnimChange.changeWorkToAnim(binding.clCoolScan,binding.clCool)
            setAnim(true)
        }
        setAnim(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}