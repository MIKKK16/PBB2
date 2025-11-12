package com.example.notifikasi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.notifikasi.databinding.ActivityEditTodoBinding
import com.example.notifikasi.entity.Todo
import com.example.notifikasi.usecase.TodoUseCase
import kotlinx.coroutines.launch

class EditTodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTodoBinding
    private lateinit var todoItemId: String
    private lateinit var todoUseCase: TodoUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewBinding
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Padding agar layout tidak tertutup status/navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ambil ID todo dari Intent
        todoItemId = intent.getStringExtra("todo.item.id") ?: ""

        // Inisialisasi UseCase
        todoUseCase = TodoUseCase()

        // Load data todo
        loadTodo()

        // Event tombol update
        registerEvent()
    }

    private fun loadTodo() {
        lifecycleScope.launch {
            val data = todoUseCase.getTodo(todoItemId)
            if (data == null) {
                displayMessage("Data task yang akan diedit tidak ditemukan")
                startActivity(Intent(this@EditTodoActivity, TodoActivity::class.java))
                finish()
                return@launch
            }

            binding.title.setText(data.title)
            binding.description.setText(data.description)
        }
    }

    private fun registerEvent() {
        binding.tombolUpdate.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()

            if (title.isEmpty()) {
                displayMessage("Judul tidak boleh kosong")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                // ✅ Buat objek Todo
                val todo = Todo(
                    id = todoItemId,
                    title = title,
                    description = description
                )

                try {
                    // ✅ Panggil updateTodo dengan objek Todo
                    todoUseCase.updateTodo(todo)
                    displayMessage("Catatan berhasil diperbarui")
                    finish()
                } catch (e: Exception) {
                    displayMessage("Gagal memperbarui catatan: ${e.message}")
                }
            }
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
