package com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.adapter
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.AppsManagerFragment
import com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.interfaces.DeleteAppsI
import com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.models.AppsModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.squareup.picasso.Picasso


class AppsAdapter(
    var context: Context,
    private var deleteAppsModels: List<AppsModel>,
    var deleteAppsI: DeleteAppsI,
    private var fragmentDeleteApps: AppsManagerFragment
) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>() {
    var positions: ArrayList<Int> = ArrayList()
    var ids: ArrayList<Int> = ArrayList()
    var packages: ArrayList<Uri> = ArrayList()
    var checked = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_apps, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val name: String = deleteAppsModels[position].name
        val packagename: String = deleteAppsModels[position].packagename
        val size: String = deleteAppsModels[position].size
        holder.checktodelete.setOnCheckedChangeListener(null)
        holder.checktodelete.isChecked = deleteAppsModels[position].isCheckboxIsVisible
        val id: Int = deleteAppsModels[position].id
        holder.checktodelete.isChecked = deleteAppsModels[position].isCheckboxIsVisible
        val icon: Drawable = deleteAppsModels[position].icon
        Picasso.get().load("nothing").error(icon).placeholder(icon).into(holder.icon)
        holder.name.text = name
        holder.size.text = size
        holder.check.setOnClickListener { v: View? -> holder.checktodelete.performClick() }
        holder.checktodelete.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            deleteAppsModels[position].isCheckboxIsVisible = isChecked
            val packageURI: Uri = Uri.parse("package:$packagename")
            fragmentDeleteApps.binding.mbDelete.setOnClickListener { v ->
                deleteAppsI.delete(
                    positions,
                    packages,
                    ids
                )
                packages.clear()
            }
            if (isChecked) {
                ids.add(id)
                checked += 1
                positions.add(holder.adapterPosition)
                packages.add(packageURI)
                deleteAppsI.checked(checked)

            } else {
                if(checked!=0){
                    checked-=1
                }
                deleteAppsI.checked(checked)
                ids.removeIf { inttorem: Int ->
                    return@removeIf inttorem == id
                }
                positions.removeIf { positiontoremove: Int ->
                    return@removeIf positiontoremove == position
                }
                packages.removeIf { positiontoremove: Uri ->
                    return@removeIf positiontoremove == packageURI
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return deleteAppsModels.size
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tv_app_name)
        var size: TextView = view.findViewById(R.id.tv_app_size)
        var check: ConstraintLayout = view.findViewById(R.id.cl_full)
        var icon: ImageView = view.findViewById(R.id.iv_app_icon)
        var checktodelete: MaterialCheckBox = view.findViewById(R.id.mcb_delete_apps)

    }

}