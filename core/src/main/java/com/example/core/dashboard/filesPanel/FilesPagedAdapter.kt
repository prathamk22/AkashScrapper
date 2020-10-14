package com.example.core.dashboard.filesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core.R
import com.example.data.database.FileData
import kotlinx.android.synthetic.main.files_item.view.*
import org.json.JSONObject

class FilesPagedAdapter :
    PagingDataAdapter<FileData, FilesViewHolder>(object : DiffUtil.ItemCallback<FileData>() {
        override fun areItemsTheSame(oldItem: FileData, newItem: FileData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FileData, newItem: FileData): Boolean {
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
    fun bind(item: FileData?) {
        with(itemView) {
            try {
                val jsonObject = JSONObject(item?.document_contributor ?: "")
                val name = jsonObject.getString("name")
                ownerName.text = name
            } catch (e: Exception) {
                ownerName.text = "${item?.document_contributor}"
            }
            fileName.text = item?.document_title

            fileType.text = when (item?.document_category_id) {
                1 -> "Notes"
                2 -> "Question Paper"
                3 -> "Practical Files"
                4 -> "Ebooks"
                else -> "Study Material"
            }

            setOnClickListener {
                item?.document_absolute_path?.let { url ->
                    onClick?.onFileClicked(
                        url,
                        item.document_title,
                        item.document_id
                    )
                }
            }

            wishlist.setOnClickListener {
                onClick?.onDrawableClick(
                    1,
                    item?.document_title ?: "",
                    item?.document_id ?: 0,
                    item?.document_absolute_path ?: ""
                )
            }

            downloadCloud.setOnClickListener {
                onClick?.onDrawableClick(
                    2,
                    item?.document_title ?: "",
                    item?.document_id ?: 0,
                    item?.document_absolute_path ?: ""
                )
            }
        }
    }
}

interface FileClickListener {
    fun onFileClicked(url: String, name: String, fileId: Int)

    fun onDrawableClick(type: Int, fileName: String, fileId: Int, fileUrl: String)
}