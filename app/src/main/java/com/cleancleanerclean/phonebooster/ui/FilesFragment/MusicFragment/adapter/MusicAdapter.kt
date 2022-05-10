package com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.interfaces.ICheckbox
import com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment.MusicFragment
import com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment.model.MusicModel
import com.google.android.material.checkbox.MaterialCheckBox
import java.io.File


class MusicAdapter(
    var context: Context,
    var musicList: List<MusicModel>,
    checkboxInterfaces: ICheckbox,
    var fragmentSongs: MusicFragment
) :
    RecyclerView.Adapter<MusicAdapter.SongsAdapterViewHolder>() {
    private var files = ArrayList<File>()
    private var Uris = ArrayList<Uri>()
    var ids = ArrayList<Int>()
    var positions = ArrayList<Int>()
    var checkboxInterface: ICheckbox = checkboxInterfaces
    private var delete = false
    var checked = 0

    class SongsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var duration: TextView
        var size: TextView
        var checktodelete: MaterialCheckBox
        var check: RelativeLayout
        var ivsong: ImageView

        init {
            ivsong = itemView.findViewById(R.id.iv_song)
            check = itemView.findViewById(R.id.rl_full)
            title = itemView.findViewById(R.id.name)
            size = itemView.findViewById(R.id.size)
            duration = itemView.findViewById(R.id.duration)
            checktodelete = itemView.findViewById(R.id.mcb_songs)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsAdapterViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_songs, parent, false)
        return SongsAdapterViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: SongsAdapterViewHolder, position: Int) {
        val name: String = musicList[position].name
        val duration: String = musicList[position].duration
        val size: String = musicList[position].size
        holder.checktodelete.setOnCheckedChangeListener(null)
        holder.checktodelete.isChecked = musicList[position].isCheckboxIsVisible
        holder.checktodelete.isChecked = musicList[position].isCheckboxIsVisible
        val path: String = musicList[position].path
        val id: Int = musicList[position].id
        val upToNCharacters = name.substring(0, Math.min(name.length, 25))
        holder.checktodelete.setOnCheckedChangeListener(null)
        holder.checktodelete.isChecked = musicList[position].isChecked
        holder.checktodelete.isChecked = musicList[position].isCheckboxIsVisible
        holder.title.setOnClickListener { holder.checktodelete.performClick() }
        holder.ivsong.setOnClickListener { holder.checktodelete.performClick() }
        if (name.length > 25) {
            holder.title.text = "$upToNCharacters..."
        } else {
            holder.title.text = name
        }
        val fpath = File(path)
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            fpath
        )
        holder.check.setOnClickListener { holder.checktodelete.performClick() }
        holder.checktodelete.setOnCheckedChangeListener { buttonView, isChecked ->
            musicList[position].isCheckboxIsVisible = isChecked
            if (isChecked) {
                files.add(fpath)
                ids.add(id)
                Uris.add(uri)
                checked += 1
                positions.add(holder.adapterPosition)
                fragmentSongs.binding.ivShare.setOnClickListener { v ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.action = Intent.ACTION_SEND_MULTIPLE
                    intent.type = "audio/*"
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.putExtra(Intent.EXTRA_STREAM, Uris)
                    context.startActivity(Intent.createChooser(intent, "share"))
                }
                fragmentSongs.binding.ivDelete.setOnClickListener { v ->
                    for (i in files.indices) {
                        if (files[i].delete()) {
                            delete = false
                            MediaScannerConnection.scanFile(
                                context, arrayOf(files[i].path),
                                null, null
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Cannot delete" + files[i].name,
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
                    if (positions.contains(musicList[position].id)) {
                        positions.removeAt(positions.indexOf(musicList[position].id))
                    }
                    checked -= 1
                }
            }
            val handler = Handler()
            val delay = 1 // 1000 milliseconds == 1 second
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (checked >= 1) {
                        fragmentSongs.binding.rlShareOrDeleteSongs.visibility = View.VISIBLE
                    } else {
                        fragmentSongs.binding.rlShareOrDeleteSongs.visibility = View.GONE
                    }
                    handler.postDelayed(this, delay.toLong())
                }
            }, delay.toLong())
        }
        holder.duration.text = duration
        holder.size.text = size
    }

    override fun onBindViewHolder(
        holder: SongsAdapterViewHolder,
        position: Int,
        payload: List<Any>
    ) {
        if (payload.isEmpty()) {
            super.onBindViewHolder(holder, position, payload)
        } else {
//            val name: String = musicList[position].getName()
//            val duration: String = musicList[position].getDuration()
//            val size: String = musicList[position].getSize()
//            holder.checktodelete.setOnCheckedChangeListener(null)
//            holder.checktodelete.isChecked = musicList[position].checkboxIsVisible
//            holder.checktodelete.isChecked = musicList[position].isCheckboxIsVisible()
//            val path: String = musicList[position].getPath()
//            val id: Int = musicList[position].getId()
//            val upToNCharacters = name.substring(0, Math.min(name.length, 25))
//            holder.checktodelete.setOnCheckedChangeListener(null)
//            holder.checktodelete.isChecked = musicList[position].checkboxIsVisible
//            holder.checktodelete.isChecked = musicList[position].isCheckboxIsVisible()
//            holder.title.setOnClickListener { v: View? -> holder.checktodelete.performClick() }
//            holder.ivsong.setOnClickListener { v: View? -> holder.checktodelete.performClick() }
//            if (name.length > 25) {
//                holder.title.text = "$upToNCharacters..."
//            } else {
//                holder.title.text = name
//            }
//            val fpath = File(path)
//            val uri = FileProvider.getUriForFile(
//                context,
//                context.applicationContext.packageName + ".provider",
//                fpath
//            )
//            holder.check.setOnClickListener { v: View? -> holder.checktodelete.performClick() }
//            holder.checktodelete.setOnCheckedChangeListener { buttonView, isChecked ->
//                musicList[position].setCheckboxIsVisible(isChecked)
//                if (isChecked) {
//                    files.add(fpath)
//                    ids.add(id)
//                    Uris.add(uri)
//                    checked += 1
//                    positions.add(holder.adapterPosition)
//                    fragmentSongs.binding.ivShare.setOnClickListener { v ->
//                        val intent = Intent(Intent.ACTION_VIEW)
//                        intent.action = Intent.ACTION_SEND_MULTIPLE
//                        intent.type = "audio/*"
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                        intent.putExtra(Intent.EXTRA_STREAM, Uris)
//                        context.startActivity(Intent.createChooser(intent, "share"))
//                    }
//                    fragmentSongs.binding.ivDelete.setOnClickListener { v ->
//                        checkboxInterface.notchecked(true)
//                        for (i in files.indices) {
//                            if (files[i].delete()) {
//                                delete = false
//                                MediaScannerConnection.scanFile(
//                                    context, arrayOf(files[i].path),
//                                    null, null
//                                )
//                            } else {
//                                Toast.makeText(
//                                    context,
//                                    "Cannot delete" + files[i].name,
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                        checkboxInterface.notchecked(false)
//                        checkboxInterface.delete(positions, null, ids)
//                        files.clear()
//                        ids.clear()
//                        checked = 0
//                    }
//                } else {
//                    if (checked != 0) {
//                        if (ids.contains(id)) {
//                            ids.removeAt(ids.indexOf(id))
//                        }
//                        if (Uris.contains(uri)) {
//                            Uris.remove(uri)
//                        }
//                        files.remove(fpath)
//                        if (positions.contains(musicList[position].getId())) {
//                            positions.removeAt(positions.indexOf(musicList[position].getId()))
//                        }
//                        checked -= 1
//                    }
//                }
//                val handler = Handler()
//                val delay = 1 // 1000 milliseconds == 1 second
//                handler.postDelayed(object : Runnable {
//                    override fun run() {
//                        if (checked >= 1) {
//                            fragmentSongs.binding.rlShareOrDeleteSongs.setVisibility(View.VISIBLE)
//                        } else {
//                            fragmentSongs.binding.rlShareOrDeleteSongs.setVisibility(View.GONE)
//                        }
//                        handler.postDelayed(this, delay.toLong())
//                    }
//                }, delay.toLong())
//            }
//            holder.title.setOnClickListener { }
//            holder.duration.text = duration
//            holder.size.text = size
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    fun getItem(position: Int): MusicModel {
        return musicList[position]
    }

}