package com.cleancleanerclean.phonebooster.ui.FinisheOrRecommendFragment

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.cleancleanerclean.phonebooster.App
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.FinishOrRecommenFragmentBinding

class FinishOrRecommenFragment : Fragment() {

    companion object {
        fun newInstance() = FinishOrRecommenFragment()
    }
    private var _binding: FinishOrRecommenFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: FinishOrRecommenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FinishOrRecommenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FinishOrRecommenViewModel::class.java]
        binding.tvBack.setOnClickListener {
            (requireActivity()as MainActivity).navController.navigate(R.id.action_finishOrRecommend_to_mainFragment)
        }
        whatToShow()
        whatToClick()
    }
    private fun whatToClick(){
        binding.mbtnRecommend.setOnClickListener {
            if(!App.clean){
                it.findNavController().navigate(R.id.action_finishOrRecommend_to_cleanFragment)
            }
            else if(!App.boost){
                it.findNavController().navigate(R.id.action_finishOrRecommend_to_boostFragment)

            }
            else if(!App.cool){
                it.findNavController().navigate(R.id.action_finishOrRecommend_to_coolFragment)

            }
            else if(!App.batterySaver){
                it.findNavController().navigate(R.id.action_finishOrRecommend_to_batterySaverFragment)

            }
            else{
                it.findNavController().navigate(R.id.action_finishOrRecommend_to_mainFragment)
            }
        }

    }
    private fun whatToShow(){
        if(!App.clean){
            binding.mbtnRecommend.text = getString(R.string.clean)
        }
        else if(!App.boost){
            binding.mbtnRecommend.text = getString(R.string.boost)
        }
        else if(!App.cool){
            binding.mbtnRecommend.text = getString(R.string.cool)
        }
        else if(!App.batterySaver){
            binding.mbtnRecommend.text = getString(R.string.optimize)
        }
        else{
            binding.mbtnRecommend.text = "OK"
            binding.mbtnRecommend.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.button_background))
        }
    }

}