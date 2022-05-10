package com.cleancleanerclean.phonebooster.ui.BoostFragment

import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.BoostFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobBanner
import com.cleancleanerclean.phonebooster.dry.AdmobInter
import com.cleancleanerclean.phonebooster.dry.AnimChange
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.*


class BoostFragment : Fragment() {
    private var _binding: BoostFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BoostViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BoostFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    var admobBanner = AdmobBanner()
    var admobInter =AdmobInter()
private fun showScanAnim(finish:Boolean){
    admobBanner.isLoaded.observe(requireActivity()){
        object :CountDownTimer(viewModel.timerTime,viewModel.timerPeriod){
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
                if(finish){
                    CoroutineScope(Dispatchers.Main).launch {
                            admobInter.showInter(requireActivity())
                            (requireActivity() as MainActivity).navController.navigate(R.id.action_boostFragment_to_finishOrRecommend)

                   }
                }
                else {
                    admobInter.showInter(requireActivity())
                    AnimChange.changeAnimToWork(binding.clRamScan, binding.clBoost)
                    setBoost()
                }
            }
        }.start()
    }
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
            binding.lavBoost.visibility = View.VISIBLE
        }
        showScanAnim(false)
        admobBanner.loadAdBanner(binding.adView)
        admobInter.loadInter(requireContext())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}