package com.cleancleanerclean.phonebooster.ui.FinisheOrRecommendFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

}