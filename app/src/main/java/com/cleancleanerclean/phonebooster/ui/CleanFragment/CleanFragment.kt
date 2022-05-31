package com.cleancleanerclean.phonebooster.ui.CleanFragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.*
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.cleancleanerclean.phonebooster.*
import com.cleancleanerclean.phonebooster.databinding.CleanFragmentBinding
import com.cleancleanerclean.phonebooster.dry.AdmobBanner
import com.cleancleanerclean.phonebooster.dry.AdmobInter
import com.cleancleanerclean.phonebooster.ui.activities.MainActivity
import com.cleancleanerclean.phonebooster.dry.AnimChange
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.*
import java.text.DecimalFormat

class CleanFragment : Fragment() {
    private var _binding: CleanFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    lateinit var foundAppsText:String
    var timer = 0L
    companion object {
        var prefs: SharedPreferences? = null
        fun convertSize(length: Long): String {
            val format = DecimalFormat("#.##")
            val mib = (1024 * 1024).toLong()
            val kib: Long = 1024
            if (length > mib) {
                return format.format(length / mib) + " MB"
            }
            return if (length > kib) {
                format.format(length / kib) + " KB"
            } else format.format(length) + " B"
        }
    }
    fun analyze() {
        if (!FileScanner.isRunning) {
            Thread { scan(false) }.start()
        }
    }
    private fun clean() {
        if (!FileScanner.isRunning) {
            CoroutineScope(Dispatchers.IO+ Job()).launch {
                scan(true)
            }
        }
    }

    private fun clearClipboard() {
        try {
            val mCbm =requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mCbm.clearPrimaryClip()
            } else {
                val clipService =requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("", "")
                clipService.setPrimaryClip(clipData)
            }
        } catch (e: NullPointerException) {

        }
    }
    private fun reset() {
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    }
    var totalFound= 0L
    private fun scan(delete: Boolean) {
        Looper.prepare()
        reset()
        if (prefs!!.getBoolean("clipboard", false)) clearClipboard()
   setFileScanner(delete)
        CoroutineScope(Dispatchers.Main+Job()).launch {
            if(timer<viewModel.timer){
            delay(viewModel.timer)
            }
            if(delete){
                admobInter.showInter(requireActivity())
                App.clean=true
                (requireActivity() as MainActivity).navController.navigate(R.id.action_cleanFragment_to_finishOrRecommend)
            }
            else{
                endOfScan()
            }
//            if (delete) binding.statusTextView.text =
//                getString(R.string.freed) + " " + convertSize(kilobytesTotal) else binding.statusTextView.text =
//                getString(R.string.found) + " " + convertSize(kilobytesTotal)
        }
        Looper.loop()
    }
    private fun setFileScanner(delete: Boolean){
        val path = Environment.getExternalStorageDirectory()

        val fs = FileScanner(path, requireContext())
            .setEmptyDir(true)
            .setAutoWhite(true)
            .setDelete(delete)
            .setCorpse(true)
            .setGUI(binding)
            .setContext(requireContext())
            .setUpFilters(
                generic = true, aggressive = true,
                apk = true
            )
        val kilobytesTotal = fs.startScan()
        totalFound = kilobytesTotal
    }
    private fun endOfScan(){
        CoroutineScope(Dispatchers.Main).launch {
            AnimChange.changeAnimToWork(binding.clMemoryScan, binding.clFound)
            binding.tvFoundMB.text = convertSize(totalFound)
            binding.tvFoundApps.text = foundAppsText
            admobInter.showInter(requireActivity())
        }
    }
    private lateinit var viewModel: CleanViewModel
    private fun setCheckBoxNew(checkbox:MaterialCheckBox, layout:ConstraintLayout){
        checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
        layout.setOnClickListener {checkbox.performClick()  }
    }
private fun setCheckBox(){
   setCheckBoxNew(binding.mcbCache, binding.clCache)
   setCheckBoxNew(binding.mcbCacheApps, binding.clApps)
   setCheckBoxNew(binding.mcbCacheFiles, binding.clFiles)
   setCheckBoxNew(binding.mcbCacheTrash, binding.clTrash)
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CleanFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    var admobBanner = AdmobBanner()
    var admobInter = AdmobInter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CleanViewModel::class.java]
      foundAppsText = binding.tvFoundApps.text.toString()+ countApps().toString()
        setCheckBox()
        launchScan()
        initPrefs()
        binding.mbtnClean.setOnClickListener {
            AnimChange.changeWorkToAnim(binding.clMemoryScan, binding.clFound)
            binding.ivCleanFinished.visibility = View.VISIBLE
            binding.tvCleanBottom.text = getString(R.string.cleanBottom)
            clean()
        }
        admobBanner.loadAdBanner(binding.adView)
        admobInter.loadInter(requireContext())
    }

    private fun initPrefs(){
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    }
    private fun isSystemPackage(pkgInfo: ApplicationInfo): Boolean {
        return pkgInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
    private fun countApps():Int{
        val pm: PackageManager =requireActivity().packageManager
var countApps =0
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (packageInfo in packages) {
            if(!isSystemPackage(packageInfo)){
        countApps++
            }
        }
        return countApps
    }
private fun launchScan(){
    CoroutineScope(Dispatchers.IO).launch {
        scan(false)
        countTimer()
    }
}
private fun countTimer(){
    object :CountDownTimer(viewModel.timer,viewModel.timerPeriod){
        override fun onTick(p0: Long) {
timer=viewModel.timer
        }

        override fun onFinish() {

        }

    }.start()
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}