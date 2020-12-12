package com.tushe.wallapoor.ui.main.products

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.inflate
import com.tushe.wallapoor.network.models.User

class ProductsFragment : Fragment() {
    // Delegado que maneja los eventos de usuario sobre el fragmento
    private var delegate: ProductsFragmentDelegate? = null

    /**
     * PROTOCOLS
     */

    // Protocolo delegado
    interface ProductsFragmentDelegate {
    }


    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        // ViewModel
        lateinit var viewModel: ProductsViewModel

        // Al hacer apply podemos incluirle argumentos en su interior
        fun newInstance(context: Context, user: User) = ProductsFragment().apply {
            viewModel = ProductsViewModel(context)
            ProductsViewModel.user = user
        }
    }


    /**
     * LIFE CYCLE
     */

    // Liga el fragmento a la actividad
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Comprobamos que el contexto de la actividad implementa el protocolo
        if (context is ProductsFragmentDelegate)
            delegate = context
        else
            throw IllegalArgumentException("Context doesn't implement ${ProductsFragmentDelegate::class.java.canonicalName}")
    }

    // Una vez creada la instancia del fragmento
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.main_fragment)
    }

    // Marca la disponibilidad de la vista tras su creacion
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*labelCreateAccount.setOnClickListener {
            signInInteractionListener?.onGoToSignUp()
        }*/

        viewModel.viewWasCreated()
    }
}