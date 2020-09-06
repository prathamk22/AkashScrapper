package com.example.akashscrapper.dashboard.classesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.akashscrapper.R
import com.example.akashscrapper.database.Semester
import kotlinx.android.synthetic.main.classes_item.view.*

class ClassesAdapter : ListAdapter<Semester, ClassesAdapter.ClassesViewHolder>(SemesterDC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ClassesViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.classes_item, parent, false)
    )

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ClassesViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), OnClickListener {

        override fun onClick(v: View?) {

            if (adapterPosition == RecyclerView.NO_POSITION) return

            val clicked = getItem(adapterPosition)
        }

        fun bind(item: Semester) = with(itemView) {
            var initals = ""
            for (text in item.branchName.split(" "))
                if (text.isNotEmpty())
                    initals += text[0]
            classInitials.text = initals
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