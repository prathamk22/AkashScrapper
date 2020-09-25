package com.example.core.dashboard.classesPanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.R
import com.example.core.utils.PreferenceHelper.Companion.getPrefs
import com.example.data.database.Subject
import kotlinx.android.synthetic.main.subject_item.view.*

class SubjectsAdapter : ListAdapter<Subject, SubjectsAdapter.SubjectsViewHolder>(
    SubjectsDC()
) {

    var subjectClickListener: SubjectClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubjectsViewHolder(
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

        fun bind(item: Subject) = with(itemView) {
            subjectName.text = item.subjectName
            background = ContextCompat.getDrawable(
                context,
                if (item.id == getPrefs(context).SP_SELECTED_SUBJECT)
                    R.drawable.subjects_gradient_background_colored
                else
                    R.drawable.light_grey_background_rounded
            )
            subjectName.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (item.id == getPrefs(context).SP_SELECTED_SUBJECT)
                        R.color.white
                    else
                        R.color.black
                )
            )
            setOnClickListener {
                subjectClickListener?.onSubjectClicked(item)
            }
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

interface SubjectClickListener {
    fun onSubjectClicked(subject: Subject)
}