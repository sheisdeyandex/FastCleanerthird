package com.cleancleanerclean.phonebooster.ui.FilesFragment.ImagesFragment

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.ImagesFragmentBinding
import com.cleancleanerclean.phonebooster.databinding.VideosFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobBanner
import com.cleancleanerclean.phonebooster.interfaces.ICheckbox
import com.cleancleanerclean.phonebooster.ui.FilesFragment.ImagesFragment.adapter.ImagesAdapter
import com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment.adapter.VideosAdapter
import com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment.model.VideosModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImagesFragment : Fragment(),ICheckbox {

    companion object {
        fun newInstance() = ImagesFragment()
    }
    private var uniqueId = 0
    private fun getUniqueId(): Int {
        return uniqueId++
    }
    private var _binding: ImagesFragmentBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ImagesViewModel
    private lateinit var videosList: ArrayList<VideosModel>
    lateinit var videosAdapter: ImagesAdapter
    private fun getVideoList() {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            videosList.clear()
            CoroutineScope(Dispatchers.IO).launch {
                while (cursor.moveToNext()) {
                    val path =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    val myMusic = VideosModel(path, getUniqueId())
                    videosList.add(myMusic)
                    if(cursor.count==videosList.size){
                        videosAdapter = ImagesAdapter(requireContext(), videosList, this@ImagesFragment, this@ImagesFragment)
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.recycler1.isNestedScrollingEnabled = false
                            binding.recycler1.layoutManager = GridLayoutManager(requireContext(), 3)
                            binding.recycler1.adapter = videosAdapter
                            binding.pbLoadingContent.visibility = View.GONE
                        }
                        break
                    }
                }
                cursor.close()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ImagesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    var admobBanner = AdmobBanner()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ImagesViewModel::class.java]
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