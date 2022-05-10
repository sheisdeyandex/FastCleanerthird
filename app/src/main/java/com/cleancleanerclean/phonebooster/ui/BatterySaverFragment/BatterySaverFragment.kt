package com.cleancleanerclean.phonebooster.ui.BatterySaverFragment

import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.BatterySaverFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobBanner
import com.cleancleanerclean.phonebooster.dry.AdmobInter
import com.cleancleanerclean.phonebooster.dry.AnimChange
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BatterySaverFragment : Fragment() {
    private var _binding: BatterySaverFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = BatterySaverFragment()
    }
    private lateinit var viewModel: BatterySaverViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BatterySaverFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BatterySaverViewModel::class.java]
        initClickLayouts()
        initRadioButtons()
        initAnim()
        admobInter.loadInter(requireContext())
        binding.mbtnSaveBattery.setOnClickListener {
            setCountAppsAnim()
        }
    }
    var admobInter = AdmobInter()
    var admobBanner = AdmobBanner()
    private fun initAnim (){
    admobBanner.loadAdBanner(binding.adView)
    admobBanner.isLoaded.observe(requireActivity()){
        CoroutineScope(Dispatchers.Main).launch {
            delay(viewModel.timer)
            admobInter.showInter(requireActivity())
            AnimChange.changeAnimToWork(
                binding.clBatterySaverScan,
                binding.clBatterySaver
                )
            }
        }
}
    private fun setCountAppsAnim(){
            AnimChange.changeWorkToAnim(
                binding.clBatterySaverScan,
                binding.clBatterySaver
            )
        viewModel.finishFragment.observe(requireActivity()){it1->
            if(it1){
                changeBrigtness()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    admobInter.showInter(requireActivity())
                    view?.findNavController()?.navigate(R.id.action_batterySaverFragment_to_finishOrRecommend)
                }
                    }
                }

            viewModel.countApps(requireContext())
            viewModel.countText.observe(requireActivity()){
                binding.tvBatterySaverBottom.text = it
            }

    }
    private fun changeBrigtness(){
        if(binding.mrbNormalMode.isChecked){
            viewModel.setBrightness(200, requireContext())
        }
        if(binding.mrbMiddleMode.isChecked){
            viewModel.setBrightness(150, requireContext())
        }
        if(binding.mrbMaxMode.isChecked){
            viewModel.setBrightness(80, requireContext())
        }
    }
    private fun initClickLayouts(){
        initLayoutClicks(
            binding.llNormalMode,
            binding.mrbNormalMode,
            binding.mbtnSaveBattery
        )
        initLayoutClicks(
            binding.llMiddleMode,
            binding.mrbMiddleMode,
            binding.mbtnSaveBattery
        )
        initLayoutClicks(
            binding.llMaxMode,
            binding.mrbMaxMode,
            binding.mbtnSaveBattery
        )
        binding.mrbMiddleMode.buttonTintList = viewModel.setColorStateList()
        binding.mrbMiddleMode.invalidate()
        binding.mrbNormalMode.buttonTintList = viewModel.setColorStateList()
        binding.mrbNormalMode.invalidate()
        binding.mrbMaxMode.buttonTintList = viewModel.setColorStateList()
        binding.mrbMaxMode.invalidate()
    }
    private fun initLayoutClicks(linearLayout: LinearLayout, radioButton: MaterialRadioButton, button: MaterialButton){
        linearLayout.setOnClickListener {
            radioButton.performClick()
            button.isClickable=true
        }
    }
    private fun initRadioButtons(){
        changeLightningColor(
            binding.mrbNormalMode,
            binding.mrbMiddleMode,
            binding.mrbMaxMode,
            binding.ivLightning
        )
        changeLightningColor(
            binding.mrbMiddleMode,
            binding.mrbNormalMode,
            binding.mrbMaxMode,
            binding.ivLightningMiddle
        )
        changeLightningColor(
            binding.mrbMaxMode,
            binding.mrbNormalMode,
            binding.mrbMiddleMode,
            binding.ivLightningMax
        )
    }
    private fun changeLightningColor(
        radioButton: MaterialRadioButton,
        radioButtonMiddle: MaterialRadioButton,
        radioButtonMax: MaterialRadioButton,
        lightning: ImageView
    )
    {
        radioButton.setOnCheckedChangeListener { button, checked ->
            button.setOnClickListener {
                checkRadioClick(radioButtonMiddle,radioButtonMax)
            }
            if(checked){
                animateColorFill(
                    viewModel.uncheckedColor(requireContext()),
                    viewModel.checkedColor(requireContext()),
                    viewModel.colorAnimDuration,lightning
                )
            }
            else{
                animateColorFill(
                    viewModel.checkedColor(requireContext()),
                    viewModel.uncheckedColor(requireContext()),
                    viewModel.colorAnimDuration,lightning
                )
            }
        }

    }
    private fun checkRadioClick(
        radioButton: MaterialRadioButton,
        radioButtonNext: MaterialRadioButton
    )
    {
        if(radioButton.isChecked){
            radioButton.isChecked = false
        }
        else if(radioButtonNext.isChecked){
            radioButtonNext.isChecked=false
        }
    }
    private fun animateColorFill(
        startColor:Int,
        endColor:Int,
        duration:Long,
        imageView: ImageView
    )
    {
        val colorAnimation= ValueAnimator.ofArgb(startColor, endColor)
        colorAnimation.duration = duration
        colorAnimation.addUpdateListener {
            imageView.setColorFilter(it.animatedValue as Int, PorterDuff.Mode.SRC_IN)
        }
        colorAnimation.start()
    }
}