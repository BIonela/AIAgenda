package com.example.aiagenda.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aiagenda.R
import com.example.aiagenda.databinding.ItemClassbookBinding
import com.example.aiagenda.model.Grade
import com.example.aiagenda.model.SchoolClass

class ClassbookAdapter :
    ListAdapter<SchoolClass, ClassbookAdapter.ClassbookViewHolder>(DiffCallback) {

    var onGradeAdded: ((Grade) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassbookAdapter.ClassbookViewHolder {
        return ClassbookViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_classbook, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ClassbookViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<SchoolClass>() {
        override fun areItemsTheSame(oldItem: SchoolClass, newItem: SchoolClass): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SchoolClass, newItem: SchoolClass): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.shortName == newItem.shortName &&
                    oldItem.percentage == newItem.percentage &&
                    oldItem.credits == newItem.credits &&
                    oldItem.exam == newItem.exam &&
                    oldItem.teacher == newItem.teacher &&
                    oldItem.courses == newItem.courses
        }
    }

    inner class ClassbookViewHolder(
        private var binding: ItemClassbookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: SchoolClass) {
            binding.tvClassName.text = course.name

            binding.etGrade.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (!p0.isNullOrBlank()) {
                        val grade =
                            Grade(
                                name = course.name,
                                grade = p0.toString().toInt(),
                                credits = course.credits
                            )
                        onGradeAdded?.invoke(grade)

                    } else {
                        onGradeAdded?.invoke(Grade(name = course.name, grade = 0, credits = 0))
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}
            })
        }
    }
}