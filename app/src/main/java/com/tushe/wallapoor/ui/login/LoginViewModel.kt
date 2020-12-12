@file:Suppress("NAME_SHADOWING")

package com.tushe.wallapoor.ui.login

import android.content.Context
import android.location.Location
import com.tushe.wallapoor.network.models.User
import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.network.managers.Managers
import com.tushe.wallapoor.network.managers.userLocation.UserLocation

class LoginViewModel(context: Context) {
    // Instancia del protocolo para delegar a la actividad
    private var delegate: LoginViewModelDelegate? = null
    private lateinit var location: Location

    /**
     * PROTOCOLS
     */

    interface LoginViewModelDelegate {
        fun locationObtained()
    }


    /**
     * STATIC INIT
     */

    init {
        // Comprobamos que el contexto de la actividad implementa el protocolo
        if (context is LoginViewModelDelegate)
            delegate = context
        else
            throw IllegalArgumentException("Context doesn't implement ${LoginViewModelDelegate::class.java.canonicalName}")
    }


    /**
     * PUBLIC FUNCTIONS
     */

    fun askForLocationPermissions(activity: LoginActivity) {
        val managerUserLocation = UserLocation()
        if (managerUserLocation.checkPermissions(activity)) {
            managerUserLocation.getLocation(activity) { location ->
                this.location = location
                delegate?.locationObtained()
            }

        } else {
            managerUserLocation.requestPermissions(activity)
        }
    }

    fun checkUserLogged(onSuccess: (User?) -> Unit) {
        // Chequea usuario logueado en UserAuthoritation
        Managers.managerUserAuthoritation?.isLogged { user ->
            onSuccess(user)
        }
    }

    fun registerUser(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?) {
        // Registra nuevo usuario en UserAuthoritation
        Managers.managerUserAuthoritation?.register(user, { user ->
            onSuccess(user)

        }, { error ->
            if (onError != null)
                onError(error)
        })
    }

    fun logUser(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?) {
        // Loguea usuario existente en UserAuthoritation
        Managers.managerUserAuthoritation?.login(user, { user ->
            onSuccess(user)

        }, { error ->
            if (onError != null)
                onError(error)
        })
    }

    fun getUserLogged(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?) {
        Managers.managerUserFirestore?.selectUser(user.sender.id, { firestoreUser ->
            if (firestoreUser != null) {
                // Devolvemos el usuario existente en Firestore BD
                onSuccess(firestoreUser)

            } else {
                // El usuario no existe en Firestore BD. Completamos datos e insertamos
                user.latitude = location.latitude
                user.longitude = location.longitude
                Managers.managerUserFirestore?.insertUser(user, {
                    // Devolvemos el usuario nuevo
                    onSuccess(user)

                }, { error ->
                    if (onError != null)
                        onError(error)
                })
            }

        }, { error ->
            if (onError != null)
                onError(error)
        })
    }

    fun recoverUser(user: User, onSuccess: () -> Unit, onError: ExceptionClosure?) {
        Managers.managerUserAuthoritation?.recoverPassword(user, {
            onSuccess()

        }, { error ->
            if (onError != null)
                onError(error)
        })
    }


    /**
     * PRIVATE FUNCTIONS
     */

    /**
     * DELEGATE METHODS
     */
}