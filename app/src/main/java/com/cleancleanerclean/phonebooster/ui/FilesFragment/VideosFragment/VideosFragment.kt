package com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cleancleanerclean.phonebooster.databinding.VideosFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobBanner
import com.cleancleanerclean.phonebooster.interfaces.ICheckbox
import com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment.adapter.VideosAdapter
import com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment.model.VideosModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VideosFragment : Fragment(), ICheckbox {
    private var uniqueId = 0
    private fun getUniqueId(): Int {
        return uniqueId++
    }
    private lateinit var videosList: ArrayList<VideosModel>
    lateinit var videosAdapter: VideosAdapter
    private fun getVideoList() {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            videosList.clear()
            CoroutineScope(Dispatchers.IO).launch {
                while (cursor.moveToNext()) {
                    val path =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                    val myMusic = VideosModel(path, getUniqueId())
                    videosList.add(myMusic)
                    if(cursor.count==videosList.size){
                        videosAdapter = VideosAdapter(requireContext(), videosList, this@VideosFragment, this@VideosFragment)
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.recycler1.isNestedScrollingEnabled = false
                            binding.recycler1.layoutManager = GridLayoutManager(requireContext(), 3)
                            binding.recycler1.adapter = videosAdapter
                            binding.pbLoadingContent.visibility = View.GONE
                        }
                    }
                }
                cursor.close()
            }
        }
    }
    private var _binding: VideosFragmentBinding? = null
    val binding get() = _binding!!
    companion object {
        fun newInstance() = VideosFragment()
    }
    private lateinit var viewModel: VideosViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VideosFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    var admobBanner = AdmobBanner()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[VideosViewModel::class.java]
        videosList = ArrayList()
        getVideoList()
        binding.ivBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        admobBanner.loadAdBanner(binding.adView)
        admobBanner.loadAdBanner(binding.adViewBottom)
    }

    override fun checked(i: Int) {
        if (i >= 1) {
            binding.rlShareOrDeleteSongs.visibility = View.VISIBLE
        } else {
            binding.rlShareOrDeleteSongs.visibility = View.GONE
        }
    }

    override fun notChecked(deleteProgress: Boolean) {
    }

    override fun delete(integers: ArrayList<Int>, uris: ArrayList<Uri?>?, ids: ArrayList<Int>) {
        for (i in integers) {
            for (i1 in ids.indices) {
                videosList.removeIf { imagesModel: VideosModel ->
                    return@removeIf imagesModel.id == ids[i1]
                }
            }
            videosAdapter.notifyItemRemoved(i)
            videosAdapter.notifyItemRangeChanged(i, videosList.size)
        }
    }

    override fun share(Uris: Boolean) {
    }
}