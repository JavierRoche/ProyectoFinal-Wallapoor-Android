package com.tushe.wallapoor.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.isFirstTimeCreated
import com.tushe.wallapoor.network.managers.Managers
import com.tushe.wallapoor.network.managers.userFirestore.UserAuthoritation
import com.tushe.wallapoor.network.models.User
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        const val OBJECT_SERIALIZABLE = "EXTRA_OBJECT_SERIALIZABLE"

        // Metodo estatico que devuelve el Intent de la actividad
        fun getIntent(context: Context, user: User): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.apply {
                // Pasamos el modelo de usuario logueado serializado
                this.putExtra(OBJECT_SERIALIZABLE, user)
            }
            return intent
        }
    }


    /**
     * LIFE CYCLE
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Indicamos cual es el fichero xml que manejara la actividad
        setContentView(R.layout.main_activity)

        // Comprobamos que sea la primera vez que se instancia la clase
        if (isFirstTimeCreated(savedInstanceState)) {
            // Intentamos recuperar el usuario logueado
            val user = intent.extras?.getSerializable(OBJECT_SERIALIZABLE) as User
            println(user)
        }

        logoutButton2.setOnClickListener {
            /// Arrancamos el manager y deslogueamos
            Managers.managerUserAuthoritation = UserAuthoritation()
            Managers.managerUserAuthoritation?.logout {
                Snackbar.make(container, "User logout", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}