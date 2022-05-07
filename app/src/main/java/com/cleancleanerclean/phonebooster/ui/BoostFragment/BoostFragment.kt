package com.cleancleanerclean.phonebooster.ui.BoostFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleancleanerclean.phonebooster.dry.AnimChange
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.BoostFragmentBinding
import kotlinx.coroutines.*

class BoostFragment : Fragment() {
    private var _binding: BoostFragmentBinding? = null
    private val binding get() = _binding!!
//    companion object {
//        fun newInstance() = BoostFragment()
//    }

    private lateinit var viewModel: BoostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BoostFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
private fun showScanAnim(finish:Boolean){
  object :CountDownTimer(viewModel.timerTime,viewModel.timerPeriod){
      override fun onTick(p0: Long) {
      }
      override fun onFinish() {
          if(finish){
              CoroutineScope(Dispatchers.Main).launch {
                  (requireActivity() as MainActivity).navController.navigate(R.id.action_boostFragment_to_finishOrRecommend)
              }
          }
          else {
              AnimChange.changeAnimToWork(binding.clRamScan, binding.clBoost)
              setBoost()
          }
      }
  }.start()
}

    private fun setBoost(){
        CoroutineScope(Dispatchers.Main).launch {
            binding.tvFoundMB.text = viewModel.usedMemory(requireActivity())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BoostViewModel::class.java]
        binding.mbtnBoost.setOnClickListener {
            binding.tvBoostBottom.text = getString(R.string.boostBottom)
            AnimChange.changeWorkToAnim(binding.clRamScan, binding.clBoost)
            showScanAnim(true)
        }
        showScanAnim(false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}