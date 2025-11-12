package com.example.notifikasi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.notifikasi.databinding.ActivityCreateTodoBinding
import com.example.notifikasi.entity.Todo
import com.example.notifikasi.usecase.TodoUseCase
import kotlinx.coroutines.launch

class CreateTodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTodoBinding
    private lateinit var todoUseCase: TodoUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi ViewBinding
        binding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi UseCase
        todoUseCase = TodoUseCase()

        // Atur padding agar layout tidak tertutup status/navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Event klik tombol simpan
        binding.btnCreate.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()

            if (title.isEmpty()) {
                displayMessage("Judul tidak boleh kosong")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val todo = Todo(
                    id = "", // kosong dulu, Firestore auto-generate
                    title = title,
                    description = description
                )

                try {
                    todoUseCase.createTodo(todo)
                    displayMessage("Catatan berhasil ditambahkan")
                    finish()
                } catch (e: Exception) {
                    displayMessage("Gagal menambahkan catatan: ${e.message}")
                }
            }
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
