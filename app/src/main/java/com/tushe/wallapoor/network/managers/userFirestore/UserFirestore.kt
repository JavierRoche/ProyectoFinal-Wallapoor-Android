package com.tushe.wallapoor.network.managers.userFirestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.network.models.User

class UserFirestore : UserFirestoreManager {
    // Instancia para acceder al nodo principal de la DB de Firestore
    private var db = FirebaseFirestore.getInstance().collection("users")

    override fun selectUsers(onSuccess: (List<User>) -> Unit, onError: ExceptionClosure) {
        // Realizamos la SELECT a Firebase.users ordenando por nombre de usuario
        db.orderBy("username").get().addOnSuccessListener { snapshot ->
            val users: List<User> = snapshot.documents.map { document ->
                // Mapeamos cada QueryDocumentSnapshot a un objeto User
                User.mapper(document = document as QueryDocumentSnapshot)
            }
            onSuccess(users)

        }.addOnFailureListener { error ->
            onError(error)
        }
    }

    override fun selectUser(userId: String, onSuccess: (User?) -> Unit, onError: ExceptionClosure) {
        // Comprobamos la presencia del usuario en BD
        db.whereEqualTo("userid", userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.documents.isEmpty()) {
                onSuccess(null)

            } else {
                // Mapeamos el QueryDocumentSnapshot al modelo
                onSuccess(User.mapper(snapshot.first()))
            }

        }.addOnFailureListener { error ->
            onError(error)
        }
    }

    override fun insertUser(user: User, onSuccess: () -> Unit, onError: ExceptionClosure) {
        // Pasamos el usuario al tipo QueryDocumentSnapshot
        val snapshot = User.toSnapshot(user)

        // Insertamos el usuario en Firestore BD
        db.add(snapshot).addOnSuccessListener {
            onSuccess()

        }.addOnFailureListener { error ->
            onError(error)
        }
    }

    override fun updateUser(user: User, onSuccess: () -> Unit, onError: ExceptionClosure) {
        // Comprobamos la presencia del usuario en BD
        db.whereEqualTo("userid", user.sender.id).get().addOnSuccessListener { snapshot ->
            // Pasamos el user modificado al tipo QueryDocumentSnapshot
            val updatedSnapshot = User.toSnapshot(user)

            // Actualizamos el usuario en BD
            val document = snapshot.first()
            db.document(document.id).set(updatedSnapshot).addOnSuccessListener {
                onSuccess()

            }.addOnFailureListener { error ->
                onError(error)
            }

        }.addOnFailureListener { error ->
            onError(error)
        }
    }
}