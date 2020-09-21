package com.example.akashscrapper.dashboard.classesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.akashscrapper.R
import com.example.data.database.Subject
import kotlinx.android.synthetic.main.subject_item.view.*

class SubjectsAdapter : ListAdapter<com.example.data.database.Subject, SubjectsAdapter.SubjectsViewHolder>(SubjectsDC()) {

    var subjectClickListener: SubjectClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubjectsViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_item, parent, false)
    )

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.subjectClickListener = subjectClickListener
    }

    class SubjectsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        var subjectClickListener: SubjectClickListener? = null

        fun bind(item: com.example.data.database.Subject) = with(itemView) {
            subjectName.text = item.subjectName
            setOnClickListener {
                subjectClickListener?.onSubjectClicked(item)
            }
        }
    }

    private class SubjectsDC : DiffUtil.ItemCallback<com.example.data.database.Subject>() {
        override fun areItemsTheSame(
            oldItem: com.example.data.database.Subject,
            newItem: com.example.data.database.Subject
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: com.example.data.database.Subject,
            newItem: com.example.data.database.Subject
        ): Boolean {
            return oldItem == newItem
        }
    }
}

interface SubjectClickListener {
    fun onSubjectClicked(subject: com.example.data.database.Subject)
}