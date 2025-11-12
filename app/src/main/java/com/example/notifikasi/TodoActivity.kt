package com.example.notifikasi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notifikasi.adapter.TodoAdapter
import com.example.notifikasi.databinding.ActivityTodoBinding
import com.example.notifikasi.entity.Todo
import com.example.notifikasi.usecase.TodoUseCase
import kotlinx.coroutines.launch

class TodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var todoUseCase: TodoUseCase
    private lateinit var adapter: TodoAdapter
    private val todoList = mutableListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur padding agar layout tidak tertutup status/navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi UseCase
        todoUseCase = TodoUseCase()

        // Setup Adapter dengan interface
        adapter = TodoAdapter(todoList, object : TodoAdapter.TodoItemEvents {
            override fun onDelete(todo: Todo) {
                deleteTodo(todo)
            }

            override fun onEdit(todo: Todo) {
                val intent = Intent(this@TodoActivity, EditTodoActivity::class.java)
                intent.putExtra("todo.item.id", todo.id)
                startActivity(intent)
            }
        })

        // Setup RecyclerView
        binding.container.layoutManager = LinearLayoutManager(this)
        binding.container.adapter = adapter

        // Event tombol tambah
        binding.TombolCreateTodo.setOnClickListener {
            startActivity(Intent(this, CreateTodoActivity::class.java))
        }

        // Load data todo
        loadTodos()
    }

    override fun onResume() {
        super.onResume()
        loadTodos()
    }

    private fun loadTodos() {
        lifecycleScope.launch {
            try {
                binding.uiLoading.visibility = View.VISIBLE
                val data = todoUseCase.getTodo()
                todoList.clear()
                todoList.addAll(data)
                adapter.updateData(todoList)
                binding.uiLoading.visibility = View.GONE
            } catch (e: Exception) {
                binding.uiLoading.visibility = View.GONE
                displayMessage("Gagal mengambil data: ${e.message}")
            }
        }
    }

    private fun deleteTodo(todo: Todo) {
        lifecycleScope.launch {
            try {
                todoUseCase.deleteTodo(todo.id)
                displayMessage("Catatan dihapus")
                loadTodos()
            } catch (e: Exception) {
                displayMessage("Gagal menghapus catatan: ${e.message}")
            }
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
