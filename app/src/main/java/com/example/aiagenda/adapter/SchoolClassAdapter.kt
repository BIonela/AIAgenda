package com.example.aiagenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aiagenda.R
import com.example.aiagenda.databinding.ItemClassBinding
import com.example.aiagenda.model.SchoolClass

class SchoolClassAdapter : ListAdapter<SchoolClass, SchoolClassAdapter.ClassViewHolder>(DiffCallback) {

    var onItemClick: ((SchoolClass) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SchoolClassAdapter.ClassViewHolder {
        return ClassViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_class, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<SchoolClass>() {
        override fun areItemsTheSame(oldItem: SchoolClass, newItem: SchoolClass): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SchoolClass, newItem: SchoolClass): Boolean {
            return oldItem.name == newItem.name
        }
    }

    inner class ClassViewHolder(
        private var binding: ItemClassBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: SchoolClass) {
            binding.tvClassName.text = course.name

            itemView.setOnClickListener {
                onItemClick?.invoke(course)
            }

        }

    }
}