package com.cleancleanerclean.phonebooster.ui.SplashFragment

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.SplashFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobInter
import com.google.android.material.snackbar.Snackbar

class SplashFragment : Fragment() {
    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun checkFirstLaunch(){
       val firstLaunch=PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("first_launch",false)
        if(firstLaunch){
            binding.clNotFirst.visibility= View.VISIBLE
            binding.clFirstLaunch.visibility= View.GONE
            startAnim()
        }
    }
    var admobInter = AdmobInter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        binding.tvPolicy.text = viewModel.underlinePolicyText(getString(R.string.policy))
        admobInter.loadInter(requireContext())
        checkFirstLaunch()
        binding.mbtnStart.setOnClickListener {
            if(binding.mchPolicy.isChecked){
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().putBoolean("first_launch",true).apply()
            startAnim()
            }
            else{
                Snackbar.make(it,getString(R.string.policy_bottom),Snackbar.LENGTH_LONG).show()
            }
        }

    }
    private fun startAnim(){
        binding.clNotFirst.visibility= View.VISIBLE
        val animation = ObjectAnimator.ofInt(binding.lpiProgress, "progress", 0, 100)
        animation.duration = 5000
        animation.interpolator = DecelerateInterpolator()
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                startMainScreen()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        animation.start()
    }
    fun startMainScreen(){
        admobInter.showInter(requireActivity())
        view?.findNavController()?.navigate(R.id.action_splashFragment_to_mainFragment)
    }
}