package com.example.aiagenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aiagenda.R
import com.example.aiagenda.databinding.ItemClassBinding
import com.example.aiagenda.model.Class

class ClassAdapter : ListAdapter<Class, ClassAdapter.ClassViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassAdapter.ClassViewHolder {
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

    companion object DiffCallback : DiffUtil.ItemCallback<Class>() {
        override fun areItemsTheSame(oldItem: Class, newItem: Class): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Class, newItem: Class): Boolean {
            return oldItem.name == newItem.name
        }
    }

    inner class ClassViewHolder(
        private var binding: ItemClassBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Class) {
            binding.tvClassName.text = course.name
        }

    }
}