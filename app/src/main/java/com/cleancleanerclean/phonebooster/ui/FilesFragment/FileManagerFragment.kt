package com.cleancleanerclean.phonebooster.ui.FilesFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.CoolFragmentBinding
import com.cleancleanerclean.phonebooster.databinding.FileManagerFragmentBinding

class FileManagerFragment : Fragment() {
    private var _binding: FileManagerFragmentBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = FileManagerFragment()
    }

    private lateinit var viewModel: FileManagerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FileManagerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FileManagerViewModel::class.java]
        binding.ivBack.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }
}