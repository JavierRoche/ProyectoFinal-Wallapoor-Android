package com.tushe.wallapoor.ui.main.products

import android.content.Context
import com.tushe.wallapoor.network.models.User
import com.tushe.wallapoor.ui.login.LoginViewModel

class ProductsViewModel(context: Context) {
    /**
     * PROTOCOLS
     **/

    // Protocolo con los metodos abstractos del fragmento que recogen las interaciones del usuario y que seran sobreescritos en la actividad
    interface ProductsViewModelDelegate {
    }
    
    
    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        lateinit var user: User
        private var delegate: ProductsViewModelDelegate? = null
    }

    init {
        // Comprobamos que el contexto de la actividad implementa el protocolo
        if (context is ProductsViewModelDelegate)
            delegate = context
        else
            throw IllegalArgumentException("Context doesn't implement ${ProductsViewModelDelegate::class.java.canonicalName}")
    }


    /**
     * PUBLIC FUNCTIONS
     **/

    fun viewWasCreated() {
        println("viewWasCreated")
    }
}