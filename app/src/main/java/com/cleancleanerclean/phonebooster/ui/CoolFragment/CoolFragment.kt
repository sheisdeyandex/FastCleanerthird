package com.cleancleanerclean.phonebooster.ui.CoolFragment

import android.app.ActivityManager
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.cleancleanerclean.phonebooster.App
import com.cleancleanerclean.phonebooster.dry.AnimChange
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.CoolFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobBanner
import com.cleancleanerclean.phonebooster.dry.AdmobInter
import kotlinx.coroutines.*
import java.io.File

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
    var admobBanner = AdmobBanner()
    var admobInter = AdmobInter()
    private fun setAnim(finish:Boolean){
        admobBanner.isLoaded.observe(requireActivity()){
            CoroutineScope(Dispatchers.Main).launch {
                delay(viewModel.timerTime)
                if(finish){
                    admobInter.showInter(requireActivity())
                    App.cool= true
                    view?.findNavController()?.navigate(R.id.action_coolFragment_to_finishOrRecommend)
                    }
                else{
                    admobInter.showInter(requireActivity())
                    binding.tvFoundTemperature.text = viewModel.cpuTemperature().toString()
                    AnimChange.changeAnimToWork(binding.clCoolScan,binding.clCool)
                }
            }
        }
    }
    private fun getApps(){
        val packs = requireActivity().packageManager.getInstalledPackages(0)
        CoroutineScope(Dispatchers.IO).launch {
            var count=0
            packs.indices.forEach{
                val p = packs[it]
                if (!viewModel.isSystemPackage(p)) {
                    val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)
                    val name = p.applicationInfo.loadLabel(requireActivity().packageManager)
                    val packages = p.applicationInfo.packageName
                    val size: Long = File(requireContext().packageManager.getApplicationInfo(packages,0).publicSourceDir  //.publicSourceDir
                    ).length()
                    val am = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    am.killBackgroundProcesses(packages)
                    withContext(Dispatchers.Main){
                        count++
                            if(count<10){
                                Glide.with(requireContext()).load(icon).into(binding.ivAppIcon)
                                binding.tvCoolBottom.text = name
                                binding.tvAppSize.text = MainActivity.convertSize(size)
                                delay(500)
                            }
                            else if(count>=10){
                                setAnim(true)
                            }

                    }
                }
            }
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CoolViewModel::class.java]
        admobBanner.loadAdBanner(binding.adView)
        admobInter.loadInter(requireContext())
        binding.ivBoostFinished.setImageDrawable(viewModel.initImageDrawable(requireContext()))
        binding.mbtnCool.setOnClickListener {
            AnimChange.changeWorkToAnim(binding.clCoolScan,binding.clCool)
            getApps()
        }
        setAnim(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}