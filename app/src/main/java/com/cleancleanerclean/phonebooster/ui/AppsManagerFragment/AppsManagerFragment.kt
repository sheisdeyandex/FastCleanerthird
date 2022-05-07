package com.cleancleanerclean.phonebooster.ui.AppsManagerFragment

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleancleanerclean.phonebooster.BuildConfig
import com.cleancleanerclean.phonebooster.databinding.AppsManagerFragmentBinding
import com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.adapter.AppsAdapter
import com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.interfaces.DeleteAppsI
import com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.models.AppsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AppsManagerFragment : Fragment(), DeleteAppsI {
    private var _binding: AppsManagerFragmentBinding? = null
    val binding get() = _binding!!
    companion object {
        fun newInstance() = AppsManagerFragment()
    }

    private lateinit var viewModel: AppsManagerViewModel
    lateinit var appsAdapter: AppsAdapter
    lateinit var appsModelList: ArrayList<AppsModel>
    lateinit var appname: ArrayList<String>
    lateinit var appsize: ArrayList<String>
    lateinit var appicon: ArrayList<Drawable>
    lateinit var appsModel: AppsModel
    lateinit var ids: ArrayList<Int>
    lateinit var integers1: ArrayList<Int>
    lateinit var activityResultLaunch: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AppsManagerFragmentBinding.inflate(inflater, container, false)
        initDeleteResult()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AppsManagerViewModel::class.java]
        initArrayLists()
        getPackageList()
        binding.ivBack.setOnClickListener { it.findNavController().popBackStack() }
    }

    private fun initDeleteResult(){
        activityResultLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    for (i in integers1) {
                        for (i1 in ids.indices) {
                            if(appsAdapter.checked>0) {
                                appsAdapter.checked -= 1
                            }
                            appsAdapter.deleteAppsI.checked(appsAdapter.checked)
                            appsModelList.removeIf { imagesModel: AppsModel ->
                                return@removeIf imagesModel.id == ids[i1]
                            }
                        }
                        appsAdapter.notifyItemRemoved(i)
                        appsAdapter.notifyItemRangeChanged(i, appsModelList.size)
                    }
                }
                Activity.RESULT_CANCELED -> {
                }
                Activity.RESULT_FIRST_USER -> {
                }
            }
        }
    }
    private fun initArrayLists(){
        appname = ArrayList()
        appicon = ArrayList()
        appsize = ArrayList()
        appsModelList = ArrayList()
    }
    private fun getPackageList(){
        val packList = requireActivity().packageManager.getInstalledPackages(0)
        CoroutineScope(Dispatchers.IO).launch {
            for (i in packList.indices) {
                val packInfo = packList[i]
                if (packInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    var appName=""
                    if(isAdded){
                        appName =
                            packInfo.applicationInfo.loadLabel(requireActivity().packageManager
                            ).
                            toString()
                    }

                    val packageName = packInfo.applicationInfo.packageName
                    if (packageName != BuildConfig.APPLICATION_ID) {
                        appname.add(appName)
                        try {
                            var size=0L
                            if (isAdded){
                                 size = File(
                                    requireContext().packageManager.getApplicationInfo(
                                        packageName,
                                        0
                                    ).
                                    publicSourceDir
                                ).length()
                            }

                            appsize.add(viewModel.getFileSize(size))
                            try {

                                appsModel = AppsModel()
                                if(isAdded){
                                    val icon =
                                        requireContext().
                                        packageManager.
                                        getApplicationIcon(packageName)

                                    appicon.add(icon)

                                    appsModel.icon = icon
                                }

                                appsModel.name = appName
                                appsModel.size = viewModel.getFileSize(size)
                                appsModel.id = viewModel.getUniqueId()
                                appsModel.packagename = packageName
                                appsModelList.add(appsModel)
                                CoroutineScope(Dispatchers.Main).launch {
                                    initRecycler()
                                }
                            } catch (e: PackageManager.NameNotFoundException) {
                                e.printStackTrace()
                            }
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
    private fun initRecycler(){
        binding.rvAppsRecycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvAppsRecycler.isNestedScrollingEnabled=false
        if(isAdded){
            appsAdapter = AppsAdapter(requireContext(), appsModelList, this, this)
            binding.rvAppsRecycler.adapter = appsAdapter
        }
    }
    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    override fun delete(integers: ArrayList<Int>, packages: ArrayList<Uri>, ids: ArrayList<Int>) {
        integers1 = ArrayList()
        integers1.addAll(integers)
        this.ids = ArrayList()
        this.ids.addAll(ids)
        for (i in packages) {
            val uninstallIntent = Intent(Intent.ACTION_DELETE, i)
            uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
            activityResultLaunch.launch(uninstallIntent)
        }

    }

    override fun checked(i: Int) {
        if(i>=1){
            binding.mbDelete.visibility = View.VISIBLE
        }
        else{
            binding.mbDelete.visibility = View.GONE
        }
    }

}