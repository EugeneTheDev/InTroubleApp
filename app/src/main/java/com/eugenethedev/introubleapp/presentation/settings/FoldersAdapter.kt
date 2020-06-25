package com.eugenethedev.introubleapp.presentation.settings

import com.eugenethedev.introubleapp.domain.entities.Folder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugenethedev.introubleapp.R
import io.realm.RealmList
import kotlinx.android.synthetic.main.item_folder.view.*

class FoldersAdapter(
    private val folders: RealmList<Folder>,
    private val removeFolder: (Folder) -> Unit
) : RecyclerView.Adapter<FoldersAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val vView = LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(vView)
    }

    init {
        folders.addChangeListener { _, changeSet ->
            changeSet.insertions.takeIf { it.isNotEmpty() }?.let {
                notifyItemRangeInserted(it.first(), it.size)
            }

            changeSet.deletions.takeIf { it.isNotEmpty() }?.let {
                notifyItemRemoved(it.first())
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = folders.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val receiver = folders[position]!!
        holder.bind(receiver, removeFolder)
    }

    class FolderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(folder: Folder, removeAction: (Folder) -> Unit) = itemView.apply {
            folderPath.text = folder.displayPath
            removeFolder.setOnClickListener { removeAction(folder) }
        }
    }
}
