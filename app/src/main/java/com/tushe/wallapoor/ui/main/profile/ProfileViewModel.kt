package com.tushe.wallapoor.ui.main.profile

import android.content.Context
import com.tushe.wallapoor.network.models.User

class ProfileViewModel(context: Context) {
    /**
     * PROTOCOLS
     **/

    // Protocolo con los metodos abstractos del fragmento que recogen las interaciones del usuario y que seran sobreescritos en la actividad
    interface ProfileViewModelDelegate {
    }


    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        lateinit var user: User
        private var delegate: ProfileViewModelDelegate? = null
    }

    init {
        // Comprobamos que el contexto de la actividad implementa el protocolo
        if (context is ProfileViewModelDelegate)
            delegate = context
        else
            throw IllegalArgumentException("Context doesn't implement ${ProfileViewModelDelegate::class.java.canonicalName}")
    }


    /**
     * PUBLIC FUNCTIONS
     **/


}