@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING")

package com.tushe.wallapoor.ui.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.tushe.wallapoor.BuildConfig
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.Sender
import com.tushe.wallapoor.common.isFirstTimeCreated
import com.tushe.wallapoor.common.showSnackbar
import com.tushe.wallapoor.network.managers.Managers
import com.tushe.wallapoor.network.managers.userFirestore.UserAuthoritation
import com.tushe.wallapoor.network.managers.userFirestore.UserFirestore
import com.tushe.wallapoor.network.managers.userLocation.UserLocation
import com.tushe.wallapoor.network.models.User
import com.tushe.wallapoor.ui.main.MainActivity
import kotlinx.android.synthetic.main.login_activity.*
import java.util.*

class LoginActivity: AppCompatActivity(), LoginViewModel.LoginViewModelDelegate, Animator.AnimatorListener {
    // Instancia del viewModel con el contexto de la actividad
    private var viewModel = LoginViewModel(this@LoginActivity)
    // Control para saber si esta la interface de registro abierta
    private var onRegisterInterface: Boolean = false

    /**
     * LIFE CYCLE
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        // Al crearse la actividad llamamos al creador padre
        super.onCreate(savedInstanceState)
        // Indicamos cual es el fichero xml que maneja la actividad
        this.setContentView(R.layout.login_activity)

        /// Icializamos los managers necesarios
        Managers.managerUserAuthoritation = UserAuthoritation()
        Managers.managerUserFirestore = UserFirestore()

        // Comprobamos que sea la primera vez que se instancia la clase
        if (isFirstTimeCreated(savedInstanceState)) {
            // Si el usuario esta logueado creamos escena principal
            this.viewModel.checkUserLogged { user ->
                if (user != null) {
                    // Vista de loading antes de crear la escena principal
                    this.enableLoading()
                    this.viewModel.getUserLogged(user, { user ->
                        // O usuario existente o usuario nuevo
                        this.createMainScene(user)

                    }, {
                        Snackbar.make(container, it.localizedMessage, Snackbar.LENGTH_LONG).show()
                    })
                }
            }

            // Preparacion de la interface de usuario
            this.configureUI()
        }
    }

    override fun onStart() {
        super.onStart()

        // Necesario aqui por si se viene por Settings
        this.viewModel.askForLocationPermissions(this@LoginActivity)
    }


    /**
     * USER INTERACTIONS
     **/

    private fun openRegisterInterface() {
        val animationSet = AnimatorSet()
        animationSet.addListener(this@LoginActivity)

        // Creamos las animaciones para los dos botones
        val registerTranslate = ObjectAnimator.ofFloat(registerButton, "translationY", 0.0f, 140.0f)
        registerTranslate.duration = 2000
        val loginTranslate = ObjectAnimator.ofFloat(loginButton, "translationY", 0.0f, 140.0f)
        loginTranslate.duration = 2000
        // Las ejecutamos simultaneamente
        animationSet.playTogether(registerTranslate, loginTranslate)
        animationSet.start()
    }

    private fun closeRegisterInterface() {
        val animationSet = AnimatorSet()
        animationSet.addListener(this@LoginActivity)

        // Creamos las animaciones para los dos botones
        val registerTranslate = ObjectAnimator.ofFloat(registerButton, "translationY", 0.0f, -140.0f)
        registerTranslate.duration = 2000
        val loginTranslate = ObjectAnimator.ofFloat(loginButton, "translationY", 0.0f, -140.0f)
        loginTranslate.duration = 2000
        // Las ejecutamos simultaneamente
        animationSet.playTogether(registerTranslate, loginTranslate)
        animationSet.start()
    }


    /**
     * USER INTERACTIONS
     **/

    private fun configureUI() {
        // Desactivamos la interaccion del usuario en pantalla
        this.userInteractionOff()

        // Abrimos el panel de registro o lanzamos el registro
        registerButton.setOnClickListener {
            if (!onRegisterInterface) {
                openRegisterInterface()

            } else {
                register()
            }
        }

        // Lanzamos el login de usuario
        loginButton.setOnClickListener { login() }
        // Ocultamos el panel de registro
        hideButton.setOnClickListener { closeRegisterInterface() }
        // Lanzamos la recuperacion de contraseña de usuario
        recoverButton.setOnClickListener { recover() }
        // TODO: QUITAR
        /*logoutButton.setOnClickListener {
            Managers.managerUserAuthoritation?.logout {
                Snackbar.make(container, "User logout", Snackbar.LENGTH_LONG).show()
            }
        }*/
    }


    /**
     * PRIVATE FUNCTIONS
     **/

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

    private fun isValidData(register: Boolean = false) : Boolean {
        if (inputEmail.text.isEmpty()) {
            inputEmail.error = getString(R.string.error_empty)
            return false
        }

        if (inputPassword.text.isEmpty()) {
            inputPassword.error = getString(R.string.error_empty)
            return false
        }

        if (register && inputUsername.text.isEmpty()) {
            inputUsername.error = getString(R.string.error_empty)
            return false
        }

        if (register && inputUsername.text.toString().length < 5) {
            inputUsername.error = getString(R.string.error_length)
            return false
        }
        return true
    }

    private fun register() {
        // Ocultamos el teclado
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(container.windowToken, 0)
        this.enableLoading()

        // Chequeamos la validez de los datos
        if (!this.isValidData(true)) return

        val email = inputEmail.text.toString().toLowerCase(Locale.ROOT)
        val password = inputPassword.text.toString()
        val username = inputUsername.text.toString()

        // Inicializamos un User con los datos introducidos por el usuario y registramos
        val user = User(Sender(String(), email), email, password)

        this.viewModel.registerUser(user, { user ->
            // El nuevo user aun no existira en Firestore pero se creara
            user.username = username
            this.viewModel.getUserLogged(user, { user ->
                // Usuario nuevo y logueado
                this.createMainScene(user)

            }, {
                Snackbar.make(container, it.localizedMessage, Snackbar.LENGTH_LONG).show()
            })

        }, {
            Snackbar.make(container, it.localizedMessage, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun login() {
        // Ocultamos el teclado
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(container.windowToken, 0)
        this.enableLoading()

        // Chequeamos la validez de los datos
        if (!this.isValidData()) return

        val email = inputEmail.text.toString().toLowerCase(Locale.ROOT)
        val password = inputPassword.text.toString()

        // Inicializamos un User con los datos introducidos por el usuario y logueamos
        val user = User(Sender(String(), email), email, password)

        this.viewModel.logUser(user, {
            // Completamos el usuario Auth recuperado y lo buscamos en Firestore
            this.viewModel.getUserLogged(user, { user ->
                // Usuario existente y logueado
                this.createMainScene(user)

            }, {
                Snackbar.make(container, it.localizedMessage, Snackbar.LENGTH_LONG).show()
            })

        }, {
            Snackbar.make(container, it.localizedMessage, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun recover() {
        // Chequeamos la validez de los datos
        if (inputEmail.text.isEmpty()) {
            inputEmail.error = getString(R.string.error_empty)
            return
        }

        // Inicializamos un User con los datos introducidos por el usuario
        val email = inputEmail.text.toString().toLowerCase(Locale.ROOT)
        val user = User(Sender(String(), email), email, null)

        this.viewModel.recoverUser(user, {
            Snackbar.make(container, getString(R.string.password_recovered), Snackbar.LENGTH_LONG).show()

        }, {
            Snackbar.make(container, it.localizedMessage, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun enableLoading(enabled: Boolean = true) {
        if (enabled) {
            container.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE

        } else {
            container.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    private fun createMainScene(user: User) {
        // Liberamos memoria
        Managers.managerUserAuthoritation = null
        Managers.managerUserFirestore = null

        // Creamos la escena principal
        startActivity(MainActivity.getIntent(this, user))
        finish()
    }


    /**
     * DELEGATE METHODS
     */

    // Metodo delegado que salta cuando el usuario responde al requerimiento de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != UserLocation.REQUEST_PERMISSIONS_REQUEST_CODE) return

        when (PackageManager.PERMISSION_GRANTED) {
            grantResults[0] -> {
                this.viewModel.askForLocationPermissions(this@LoginActivity)
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

    override fun locationObtained() {
        this.userInteractionOn()
    }

    override fun onAnimationStart(animation: Animator?) {
        if (onRegisterInterface) {
            // Ocultamos interface de registro
            loginButton.isVisible = true
            hideButton.isVisible = false
            inputUsername.isVisible = false
        }
    }

    override fun onAnimationEnd(animation: Animator?) {
        if (onRegisterInterface) {
            onRegisterInterface = false

        } else {
            onRegisterInterface = true
            // Mostramos interface de registro
            loginButton.isVisible = false
            hideButton.isVisible = true
            inputUsername.isVisible = true
        }
    }

    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}
}