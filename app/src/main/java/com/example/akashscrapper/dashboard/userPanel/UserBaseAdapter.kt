package com.example.akashscrapper.dashboard.userPanel

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.filesPanel.FileClickListener
import com.example.akashscrapper.database.FileData
import kotlinx.android.synthetic.main.files_item.view.*
import org.json.JSONObject

class UserBaseAdapter : ListAdapter<FileData, UserBaseAdapter.UserBaseViewHolder>(FileDataDC()) {

    var onClick: FileClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserBaseViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.files_item, parent, false)
    )

    override fun onBindViewHolder(holder: UserBaseViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.onClick = onClick
    }

    inner class UserBaseViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), OnClickListener {

        var onClick: FileClickListener? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            bindingAdapterPosition

            if (bindingAdapterPosition == RecyclerView.NO_POSITION) return

            val clicked = getItem(bindingAdapterPosition)
        }

        fun bind(item: FileData?) {
            with(itemView) {
                try {
                    val jsonObject = JSONObject(item?.document_contributor ?: "")
                    val name = jsonObject.getString("name")
                    ownerName.text = name
                } catch (e: Exception) {
                    ownerName.text = item?.document_contributor
                }
                fileName.text = item?.document_title
                val float = item?.document_size?.div(1024 * 1024)
                fileSize.text = "$float MB"
                fileViews.text = "${item?.document_view_count} Views"

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

    private class FileDataDC : DiffUtil.ItemCallback<FileData>() {
        override fun areItemsTheSame(
            oldItem: FileData,
            newItem: FileData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FileData,
            newItem: FileData
        ): Boolean {
            return oldItem.document_id == newItem.document_id
        }
    }
}