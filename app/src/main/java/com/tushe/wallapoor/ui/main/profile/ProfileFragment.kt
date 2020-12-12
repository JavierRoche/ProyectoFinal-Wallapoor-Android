package com.tushe.wallapoor.ui.main.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.inflate
import com.tushe.wallapoor.network.managers.Managers
import com.tushe.wallapoor.network.managers.userFirestore.UserAuthoritation
import com.tushe.wallapoor.network.models.User
import com.tushe.wallapoor.ui.main.products.ProductsFragment
import com.tushe.wallapoor.ui.main.products.ProductsViewModel
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {
    // Delegado que maneja los eventos de usuario sobre el fragmento
    private var delegate: ProfileFragmentDelegate? = null

    /**
     * PROTOCOLS
     */

    // Protocolo delegado
    interface ProfileFragmentDelegate {
    }


    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        // ViewModel
        lateinit var viewModel: ProfileViewModel

        // Al hacer apply podemos incluirle argumentos en su interior
        fun newInstance(context: Context, user: User) = ProfileFragment().apply {
            viewModel = ProfileViewModel(context)
            ProfileViewModel.user = user
        }
    }


    /**
     * LIFE CYCLE
     */

    // Liga el fragmento a la actividad
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Comprobamos que el contexto de la actividad implementa el protocolo
        if (context is ProfileFragmentDelegate)
            delegate = context
        else
            throw IllegalArgumentException("Context doesn't implement ${ProfileFragmentDelegate::class.java.canonicalName}")
    }

    // Una vez creada la instancia del fragmento
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.profile_fragment)
    }

    // Marca la disponibilidad de la vista tras su creacion
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: llevar a profile activiy
        logoutButton2.setOnClickListener {
            /// Arrancamos el manager y deslogueamos
            Managers.managerUserAuthoritation = UserAuthoritation()
            Managers.managerUserAuthoritation?.logout {
                Snackbar.make(fragmentContainer, "User logout", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}