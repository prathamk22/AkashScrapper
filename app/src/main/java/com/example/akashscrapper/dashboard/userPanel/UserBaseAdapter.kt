package com.example.akashscrapper.dashboard.userPanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.filesPanel.FileClickListener
import com.example.data.database.FileDownloadModel
import kotlinx.android.synthetic.main.users_pannel_list.view.*

class UserBaseAdapter :
    ListAdapter<com.example.data.database.FileDownloadModel, UserBaseAdapter.UserBaseViewHolder>(FileDataDC()) {

    var onClick: FileClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserBaseViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.users_pannel_list, parent, false)
    )

    override fun onBindViewHolder(holder: UserBaseViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.onClick = onClick
    }

    class UserBaseViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var onClick: FileClickListener? = null

        fun bind(item: com.example.data.database.FileDownloadModel?) {
            with(itemView) {
                fileName.text = item?.fileName
                setOnClickListener {
                    onClick?.onFileClicked(
                        item?.fileUrl ?: "",
                        item?.fileName ?: "",
                        item?.fileId ?: 0
                    )
                }
            }
        }
    }

    private class FileDataDC : DiffUtil.ItemCallback<com.example.data.database.FileDownloadModel>() {
        override fun areItemsTheSame(
            oldItem: com.example.data.database.FileDownloadModel,
            newItem: com.example.data.database.FileDownloadModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: com.example.data.database.FileDownloadModel,
            newItem: com.example.data.database.FileDownloadModel
        ): Boolean {
            return oldItem.fileId == newItem.fileId
        }
    }
}