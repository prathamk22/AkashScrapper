package com.example.core.dashboard.classesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.R
import com.example.core.utils.sameAndEqual
import kotlinx.android.synthetic.main.class_divider_item.view.*
import kotlinx.android.synthetic.main.classes_item.view.*

class ClassesAdapter :
    ListAdapter<SemesterListModel, ClassesAdapter.ClassesViewHolder>(
        SemesterDC()
    ) {

    var clickListener: ClassesClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ClassesViewHolder(
            when (viewType) {
                0 -> LayoutInflater.from(parent.context)
                    .inflate(R.layout.class_divider_item, parent, false)
                else -> LayoutInflater.from(parent.context)
                    .inflate(R.layout.classes_item, parent, false)
            }
        )

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SemesterListModel.Subject -> 0
            is SemesterListModel.SubjectItem -> 1
        }
    }

    override fun onBindViewHolder(holder: ClassesViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.clickListener = clickListener
    }

    class ClassesViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var clickListener: ClassesClickListener? = null
        fun bind(item: SemesterListModel) = with(itemView) {
            when(item){
                is SemesterListModel.SubjectItem ->{
                    with(item.item){
                        var initals = ""
                        for (text in branchName.split(" "))
                            if (text.isNotEmpty())
                                initals += text[0]
                        classInitials.text = initals
                        setOnClickListener {
                            clickListener?.onClick(id)
                        }
                        setOnLongClickListener {
                            clickListener?.onLongPress(id)
                            true
                        }
                    }
                }
                is SemesterListModel.Subject ->{
                    semesterTv.text = "${item.semester} Sem"
                }
            }
        }
    }

    private class SemesterDC : DiffUtil.ItemCallback<SemesterListModel>() {
        override fun areItemsTheSame(
            oldItem: SemesterListModel,
            newItem: SemesterListModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SemesterListModel,
            newItem: SemesterListModel
        ): Boolean {
            return oldItem.sameAndEqual(newItem)
        }
    }
}

interface ClassesClickListener {
    fun onClick(id: Int)
    fun onLongPress(id: Int)
}