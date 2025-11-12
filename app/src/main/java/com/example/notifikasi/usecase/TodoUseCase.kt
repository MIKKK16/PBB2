package com.example.notifikasi.usecase

import com.example.notifikasi.entity.Todo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TodoUseCase {

    private val db = FirebaseFirestore.getInstance()

    // Ambil semua data todo
    suspend fun getTodo(): List<Todo> {
        return try {
            val snapshot = db.collection("todo")
                .get()
                .await()

            snapshot.documents.map {
                Todo(
                    id = it.id,
                    title = it.getString("title").orEmpty(),
                    description = it.getString("description").orEmpty()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Ambil 1 data todo berdasarkan ID
    suspend fun getTodo(id: String): Todo? {
        return try {
            val doc = db.collection("todo")
                .document(id)
                .get()
                .await()

            if (doc.exists()) {
                Todo(
                    id = doc.id,
                    title = doc.getString("title").orEmpty(),
                    description = doc.getString("description").orEmpty()
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // Update todo
    suspend fun updateTodo(todo: Todo) {
        val data = mapOf(
            "title" to todo.title,
            "description" to todo.description
        )
        db.collection("todo")
            .document(todo.id)
            .set(data)
            .await()
    }

    // Buat todo baru
    suspend fun createTodo(todo: Todo): Todo {
        val data = mapOf(
            "title" to todo.title,
            "description" to todo.description
        )
        val docRef = db.collection("todo")
            .add(data)
            .await()

        return todo.copy(id = docRef.id)
    }

    // Hapus todo
    suspend fun deleteTodo(id: String) {
        db.collection("todo")
            .document(id)
            .delete()
            .await()
    }
}
