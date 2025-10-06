package usecase


import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import entity.Todo
import kotlinx.coroutines.tasks.await

class TodoUseCase {
    val db = Firebase.firestore

    suspend fun getTodo(): List<Todo> {
        try {
            val data = db.collection("todo")
                .get()
                .await()
            if (!data.isEmpty) {
                return data.documents.map {
                    Todo(
                        id = it.id,
                        title = it.getString("title").toString(),
                        description = it.getString("description.toString")
                    )
                }
            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
        return arrayListOf();
    }

}

