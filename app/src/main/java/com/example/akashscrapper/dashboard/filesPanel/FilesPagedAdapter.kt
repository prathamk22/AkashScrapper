package com.example.akashscrapper.dashboard.filesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.akashscrapper.R
import com.example.akashscrapper.network.models.Data
import kotlinx.android.synthetic.main.files_item.view.*

class FilesPagedAdapter :
    PagedListAdapter<Data, FilesViewHolder>(object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.document_id == newItem.document_id
        }

    }) {

    var onClick: FileClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        return FilesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.files_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.onClick = onClick
    }
}

class FilesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var onClick: FileClickListener? = null
    fun bind(item: Data?) {
        with(itemView) {
            fileName.text = item?.document_title

            setOnClickListener {
                item?.document_absolute_path?.let { url ->
                    onClick?.onFileClicked(
                        url,
                        item.document_title
                    )
                }
            }
        }
    }
}

interface FileClickListener {
    fun onFileClicked(url: String, name: String)
}