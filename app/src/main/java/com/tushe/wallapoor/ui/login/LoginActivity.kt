package com.tushe.wallapoor.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.tushe.wallapoor.BuildConfig
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.isFirstTimeCreated
import com.tushe.wallapoor.common.showSnackbar
import com.tushe.wallapoor.network.managers.userLocation.UserLocation
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity: AppCompatActivity() {
    // Instancia del viewModel
    private var viewModel = LoginViewModel()

    /**
     * LIFE CYCLE
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        // Al crearse la actividad llamamos al creador padre
        super.onCreate(savedInstanceState)
        // Indicamos cual es el fichero xml que maneja la actividad
        setContentView(R.layout.activity_login)

        // Comprobamos que sea la primera vez que se instancia la clase
        if (isFirstTimeCreated(savedInstanceState)) {
            // Si el usuario ya esta logueado vamos directamente a showTopics()
            /*if (UserRepo.isLogged(this.applicationContext)) {
                // TODO: Ir a main

            } else {
                // Indicamos con que fragmento inicia la actividad dentro del fragment_container definido en la vista xml
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, signInFragment)
                    .commit()
            }*/

            // Definimos la UI con los elementos interactuables
            configureUI()
        }
    }

    override fun onStart() {
        super.onStart()

        // Necesario aqui por si se viene por Settings
        viewModel.askForLocationPermissions(this@LoginActivity) {
            userInteractionOn()
            println(it.toString())
            // TODO something with location
        }
    }


    /**
     * USER INTERACTIONS
     **/

    private fun onAnimation() {
        val animationSet = AnimatorSet()

        // Creamos 2 animaciones
        val registerTranslate = ObjectAnimator.ofFloat(registerButton, "translationY", 0.0f, 140.0f)
        registerTranslate.duration = 2000
        val loginTranslate = ObjectAnimator.ofFloat(loginButton, "translationY", 0.0f, 140.0f)
        loginTranslate.duration = 2000
        // Las metemos en un set simultaneo
        animationSet.playTogether(registerTranslate, loginTranslate)
        animationSet.start()
    }


    /**
     * PRIVATE FUNCTIONS
     **/

    private fun configureUI() {
        // Desactivamos la interaccion del usuario en pantalla
        userInteractionOff()

        // Incluimos el evento de click al boton ROTAR
        registerButton.setOnClickListener {
            // TODO:
            onAnimation()
            //onAnimation(R.animator.register_translate)
        }
        // Incluimos el evento de click al boton ESCALAR
        loginButton.setOnClickListener {
            // TODO:
        }
        // Incluimos el evento de click al boton TRASLADAR
        recoverButton.setOnClickListener {
            // TODO:
        }
    }

    private fun userInteractionOn() {
        inputEmail.isEnabled = true
        inputPassword.isEnabled = true
        registerButton.isEnabled = true
        loginButton.isEnabled = true
        recoverButton.isEnabled = true
    }

    private fun userInteractionOff() {
        inputEmail.isEnabled = false
        inputPassword.isEnabled = false
        registerButton.isEnabled = false
        loginButton.isEnabled = false
        recoverButton.isEnabled = false
    }

    /**
     * DELEGATE METHODS
     */

    // Metodo delegado que salta cuando el usuario responde al requerimiento de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != UserLocation.REQUEST_PERMISSIONS_REQUEST_CODE) return

        when (PackageManager.PERMISSION_GRANTED) {
            grantResults[0] -> {
                viewModel.askForLocationPermissions(this@LoginActivity) {
                    userInteractionOn()
                    println(it.toString())
                    // TODO something with location
                }
            }
            else -> showSnackbar(this@LoginActivity, R.string.permission_denied_explanation, R.string.settings) {
                // Intent que mostrara la pantalla de settings del dispositivo
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
        }
    }
}