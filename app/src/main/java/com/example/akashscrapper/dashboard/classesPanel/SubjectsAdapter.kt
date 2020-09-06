package com.example.akashscrapper.dashboard.classesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.akashscrapper.R
import com.example.akashscrapper.database.Subject

class SubjectsAdapter : ListAdapter<Subject, SubjectsAdapter.SubjectsViewHolder>(SubjectsDC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubjectsViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_item, parent, false)
    )

    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class SubjectsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (adapterPosition == RecyclerView.NO_POSITION) return

            val clicked = getItem(adapterPosition)
        }

        fun bind(item: Subject) = with(itemView) {

        }
    }

    private class SubjectsDC : DiffUtil.ItemCallback<Subject>() {
        override fun areItemsTheSame(
            oldItem: Subject,
            newItem: Subject
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Subject,
            newItem: Subject
        ): Boolean {
            return oldItem == newItem
        }
    }
}