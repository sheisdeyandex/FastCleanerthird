package com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.interfaces.ICheckbox
import com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment.VideosFragment
import com.cleancleanerclean.phonebooster.ui.FilesFragment.VideosFragment.model.VideosModel
import com.google.android.material.checkbox.MaterialCheckBox
import java.io.File
import java.util.*

class VideosAdapter(
    var context: Context,
    audioList: List<VideosModel>,
    checkboxInterface: ICheckbox,
    fragmentVideos: VideosFragment
) :
    RecyclerView.Adapter<VideosAdapter.ImagesAdapterViewHolder>() {
    var videoList: List<VideosModel> = audioList
    var files = ArrayList<File>()
    var Uris = ArrayList<Uri>()
    var pathes = ArrayList<String>()
    var fragmentVideos: VideosFragment = fragmentVideos
    var ids = ArrayList<Int>()
    var positions = ArrayList<Int>()

    class ImagesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.iv_image)
        var checktodelete: MaterialCheckBox = itemView.findViewById(R.id.mcb_songs)

    }

    var checkboxInterface: ICheckbox = checkboxInterface
    var deletee = false
    var share = false
    var checked = 0
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapterViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_images, parent, false)
        return ImagesAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagesAdapterViewHolder, position: Int) {
        val objIncome: VideosModel = videoList[position]
        holder.checktodelete.setOnCheckedChangeListener(null)
        holder.checktodelete.isChecked = objIncome.isCheckboxIsVisible
        val path: String = videoList[position].path
        val fpath = File(path)
        Glide.with(context).load(path).centerCrop().into(holder.image)
        holder.checktodelete.isChecked = objIncome.isCheckboxIsVisible
        val id: Int = videoList[position].id
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            fpath
        )
        holder.image.setOnClickListener { v: View? -> holder.checktodelete.performClick() }
        holder.checktodelete.setOnCheckedChangeListener { _, isChecked ->

            objIncome.isCheckboxIsVisible= isChecked
            if (isChecked) {
                files.add(fpath)
                pathes.add(path)
                Uris.add(uri)
                checked += 1
                ids.add(id)
                positions.add(position)
                fragmentVideos.binding.ivShare.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.action = Intent.ACTION_SEND_MULTIPLE
                    intent.type = "video/*"
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.putExtra(Intent.EXTRA_STREAM, Uris)
                    context.startActivity(Intent.createChooser(intent, "share"))
                }
                fragmentVideos.binding.ivDelete.setOnClickListener {
                    for (i in files.indices) {
                        if (files[i].delete()) {
                            deletee = false
                            MediaScannerConnection.scanFile(
                                context, arrayOf(files[i].path),
                                null, null
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Cannot delete " + files[i].name,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    checkboxInterface.delete(positions, null, ids)
                    files.clear()
                    ids.clear()
                    checked = 0
                }
            } else {
                if (checked != 0) {
                    if (ids.contains(id)) {
                        ids.removeAt(ids.indexOf(id))
                    }
                    if (Uris.contains(uri)) {
                        Uris.remove(uri)
                    }
                    files.remove(fpath)
                    if (positions.contains(videoList[position].id)) {
                        positions.removeAt(positions.indexOf(videoList[position].id))
                    }
                    checked -= 1
                }
            }
            val handler = Handler()
            val delay = 1 // 1000 milliseconds == 1 second
            handler.postDelayed(object : Runnable {
                override fun run() {
                    checkboxInterface.checked(checked)
                    handler.postDelayed(this, delay.toLong())
                }
            }, delay.toLong())
        }
    }

    override fun onBindViewHolder(
        holder: ImagesAdapterViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    val data: List<Any>
        get() = videoList

}