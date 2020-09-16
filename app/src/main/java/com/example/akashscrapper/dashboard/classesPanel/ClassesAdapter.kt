package com.example.akashscrapper.dashboard.classesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.akashscrapper.R
import com.example.akashscrapper.database.Semester
import kotlinx.android.synthetic.main.classes_item.view.*

class ClassesAdapter : ListAdapter<Semester, ClassesAdapter.ClassesViewHolder>(SemesterDC()) {

    var clickListener: ClassesClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ClassesViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.classes_item, parent, false)
    )

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.clickListener = clickListener
    }

    class ClassesViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var clickListener: ClassesClickListener? = null
        fun bind(item: Semester) = with(itemView) {
            var initals = ""
            for (text in item.branchName.split(" "))
                if (text.isNotEmpty())
                    initals += text[0]
            classInitials.text = initals
            setOnClickListener {
                clickListener?.onClick(item.id)
            }
        }
    }

    private class SemesterDC : DiffUtil.ItemCallback<Semester>() {
        override fun areItemsTheSame(
            oldItem: Semester,
            newItem: Semester
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Semester,
            newItem: Semester
        ): Boolean {
            return oldItem == newItem
        }
    }
}

interface ClassesClickListener {
    fun onClick(id: Int)
}