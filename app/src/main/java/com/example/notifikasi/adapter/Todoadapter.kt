package com.example.notifikasi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notifikasi.databinding.ItemTodoBinding
import com.example.notifikasi.entity.Todo


class TodoAdapter(
    private val dataset: MutableList<Todo>,
    private val events: TodoItemEvents
) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    interface TodoItemEvents {
        fun onDelete(todo: Todo)
        fun onEdit(todo: Todo)
    }

    inner class CustomViewHolder(private val view: ItemTodoBinding)
        : RecyclerView.ViewHolder(view.root) {

        fun bindData(data: Todo) {
            view.judul.text = data.title
            view.deskripsi.text = data.description

            // Long click → hapus
            view.root.setOnLongClickListener {
                events.onDelete(data)
                true
            }

            // Click → edit
            view.root.setOnClickListener {
                events.onEdit(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val data = dataset[position]
        holder.bindData(data)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Todo>) {
        dataset.clear()
        dataset.addAll(newData)
        notifyDataSetChanged()
    }
}
