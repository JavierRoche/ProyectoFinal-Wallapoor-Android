@file:Suppress("NAME_SHADOWING")

package com.tushe.wallapoor.network.managers.userFirestore

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.common.Sender
import com.tushe.wallapoor.network.models.User

class UserAuthoritation : UserAuthorizationManager {
    override fun register(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?) {
        // Metodo que accede a Firebase a realizar un registro con email y password
        Firebase.auth.createUserWithEmailAndPassword(user.email, user.password!!).addOnCompleteListener {
            when {
                // Nos aseguramos de que ha llegado un error y la clausura de error existe
                it.exception != null && onError != null -> {
                    onError(it.exception!!)
                }
                it.isSuccessful -> {
                    // Comprobamos que el sistema ha devuelto el usuario registrado y lo devolvemos
                    val currentUser = Firebase.auth.currentUser
                    if (currentUser != null) {
                        val user = User(
                            sender = Sender(id = currentUser.uid, displayName = currentUser.email!!),
                            email = currentUser.email!!,
                            password = null
                        )
                        onSuccess(user)
                    }
                }
            }
        }
    }

    override fun login(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?) {
        // Metodo que accede a Firebase a loguear con email y password
        Firebase.auth.signInWithEmailAndPassword(user.email, user.password!!).addOnCompleteListener {
            when {
                // Nos aseguramos de que ha llegado un error y la clausura de error existe
                it.exception != null && onError != null -> {
                    onError(it.exception!!)
                }
                it.isSuccessful -> {
                    // Comprobamos que el sistema ha devuelto el usuario registrado y lo devolvemos
                    val currentUser = Firebase.auth.currentUser
                    if (currentUser != null) {
                        val user = User(
                            sender = Sender(id = currentUser.uid, displayName = currentUser.email!!),
                            email = currentUser.email!!,
                            password = null
                        )
                        onSuccess(user)
                    }
                }
            }
        }
    }

    override fun recoverPassword(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?) {
        // Enviamos el recover pass al email del user
        Firebase.auth.sendPasswordResetEmail(user.email).addOnCompleteListener {
            when {
                // Nos aseguramos de que ha llegado un error y la clausura de error existe
                it.exception != null && onError != null -> {
                    onError(it.exception!!)
                }
                it.isSuccessful -> {
                    // El sistema devuelve el usuario recuperado, pero se manda un email para el reset
                    onSuccess(user)
                }
            }
        }
    }

    override fun isLogged(onSuccess: (User?) -> Unit) {
        // Metodo que accede a Firebase a comprobar el usuario actual
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val user = User(
                sender = Sender(id = currentUser.uid, displayName = currentUser.email!!),
                email = currentUser.email!!,
                password = null
            )
            onSuccess(user)
        }
        // Sino esta logueado devolvemos un nil en la closure success
        onSuccess(null)
    }

    override fun logout(onSuccess: () -> Unit) {
        // En kotlin el metodo no lanza excepcion
        Firebase.auth.signOut()
        onSuccess()
    }
}