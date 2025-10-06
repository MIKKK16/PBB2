package adapter

import android.service.autofill.Dataset
import androidx.recyclerview.widget.RecyclerView
import entity.Todo

class Todoadapter(
    private val dataset: MutableList<Todo>
) : RecyclerView.Adapter<Todoadapter.CustomViewHolder>() {
    inner class CustomViewHolder(val view: ItemTodoBinding) : RecyclerView.ViewHolder(view.root) {
        fun bindData(item: Todo) {
            view.title.text = item.title
            view.description.text = item.description
        }
    }
}