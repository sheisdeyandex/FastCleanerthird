package com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleancleanerclean.phonebooster.databinding.MusicFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobBanner
import com.cleancleanerclean.phonebooster.interfaces.ICheckbox
import com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment.adapter.MusicAdapter
import com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment.model.MusicModel
import java.text.DecimalFormat

class MusicFragment : Fragment(),ICheckbox {
    private var _binding: MusicFragmentBinding? = null
    val binding get() = _binding!!
    companion object {
        fun newInstance() = MusicFragment()
        private fun convertSize(length: Long): String? {
            val format = DecimalFormat("#.##")
            val mb = (1024 * 1024).toLong()
            val kb: Long = 1024
            if (length > mb) {
                return format.format(length / mb) + " MB"
            }
            return if (length > kb) {
                format.format(length / kb) + " KB"
            } else format.format(length) + " B"
        }
    }

    private lateinit var viewModel: MusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MusicFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }





    var songsAdapter: MusicAdapter? = null
    var admobBanner = AdmobBanner()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MusicViewModel::class.java]
        initRecyclerView()
        binding.ivBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        admobBanner.loadAdBanner(binding.adView)
        admobBanner.loadAdBanner(binding.adViewBottom)
    }
    private fun initRecyclerView(){
        viewModel.queryMusic(requireActivity())
        val layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recycler1.layoutManager = layoutManager
        binding.recycler1.layoutManager =
            LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,
                false
            )
        songsAdapter = MusicAdapter(requireContext(),
            viewModel.musicList,
            this,
            this
        )
        binding.recycler1.adapter = songsAdapter
    }
    override fun checked(i: Int) {

    }
    override fun notChecked(deleteProgress: Boolean) {
    }
    override fun delete(integers: ArrayList<Int>, uris: ArrayList<Uri?>?, ids: ArrayList<Int>) {
        for (i in integers) {
            for (i1 in ids.indices) {
                viewModel.musicList.removeIf { imagesModel: MusicModel ->
                    return@removeIf imagesModel.id == ids[i1]
                }
            }
            i.let { songsAdapter!!.notifyItemRemoved(it) }
            i.let { songsAdapter!!.notifyItemRangeChanged(it, viewModel.musicList.size) }
        }
    }

    override fun share(Uris: Boolean) {

    }
}