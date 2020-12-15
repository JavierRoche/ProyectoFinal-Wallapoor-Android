package com.tushe.wallapoor.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.isFirstTimeCreated
import com.tushe.wallapoor.network.models.Product
import com.tushe.wallapoor.network.models.User
import com.tushe.wallapoor.ui.main.MainActivity.Companion.productsFragment
import com.tushe.wallapoor.ui.main.products.ProductsFragment
import com.tushe.wallapoor.ui.main.products.ProductsViewModel
import com.tushe.wallapoor.ui.main.profile.ProfileFragment
import com.tushe.wallapoor.ui.main.profile.ProfileViewModel
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: AppCompatActivity(),
        ProductsFragment.ProductsFragmentDelegate,
        ProfileFragment.ProfileFragmentDelegate,
        ProfileViewModel.ProfileViewModelDelegate {

    // Listener para la seleccion de navegacion
    private val onNavigationSelection = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigationShopping -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, productsFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationProfile -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, profileFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        const val OBJECT_SERIALIZABLE = "EXTRA_OBJECT_SERIALIZABLE"

        // Definimos los objetos de cada fragmento que usara la actividad
        lateinit var productsFragment: ProductsFragment
        lateinit var profileFragment: ProfileFragment

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

        // Definimos el listener que se activa al cambiar la navegacion
        navigation.setOnNavigationItemSelectedListener(onNavigationSelection)

        // Configuracion UI
        this.prepareButtons()

        // Comprobamos que sea la primera vez que se instancia la clase
        if (isFirstTimeCreated(savedInstanceState)) {
            // Intentamos recuperar el usuario logueado
            val user = intent.extras?.getSerializable(OBJECT_SERIALIZABLE) as User

            // Indicamos con que fragmento inicia la actividad
            productsFragment = ProductsFragment.newInstance(this@MainActivity, user)
            profileFragment = ProfileFragment.newInstance(this@MainActivity, user)
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, productsFragment).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Con ayuda del Inflater de menus que me llega inflamos el menu
        menuInflater.inflate(R.menu.search_bar_menu, menu)

        // Invocamos el comportamiento por defecto siempre despues de inflar el menu
        return super.onCreateOptionsMenu(menu)
    }




    /**
     * PRIVATE FUNCTIONS
     **/

    private fun prepareButtons() {
        //categoriesMenu
    }

    /**
     * DELEGATE METHODS
     **/

    override fun onCreateProduct() {
        TODO("Not yet implemented")
    }
}