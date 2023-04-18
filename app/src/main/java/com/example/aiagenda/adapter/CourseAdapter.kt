package com.example.aiagenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aiagenda.R
import com.example.aiagenda.databinding.ItemClassBinding
import com.example.aiagenda.model.PdfClass

class CourseAdapter : ListAdapter<PdfClass, CourseAdapter.CourseViewHolder>(DiffCallback) {

    var onItemClick: ((PdfClass) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseAdapter.CourseViewHolder {
        return CourseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_class, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PdfClass>() {
        override fun areItemsTheSame(oldItem: PdfClass, newItem: PdfClass): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PdfClass, newItem: PdfClass): Boolean {
            return oldItem.url == newItem.url
        }
    }

    inner class CourseViewHolder(
        private var binding: ItemClassBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: PdfClass) {
            binding.tvClassName.text = course.name

            itemView.setOnClickListener {
                onItemClick?.invoke(course)
            }

        }

    }
}