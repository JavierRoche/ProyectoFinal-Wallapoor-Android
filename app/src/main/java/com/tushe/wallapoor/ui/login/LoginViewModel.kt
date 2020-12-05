package com.tushe.wallapoor.ui.login

import android.location.Location
import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.network.managers.userLocation.UserLocation

class LoginViewModel {


    /**
     * STATIC INIT
     */

    companion object {

    }


    /**
     * PUBLIC FUNCTIONS
     */

    fun askForLocationPermissions(activity: LoginActivity, onSuccess: (Location) -> Unit) {
        val managerUserLocation = UserLocation()
        if (managerUserLocation.checkPermissions(activity)) {
            managerUserLocation.getLocation(activity) {
                onSuccess(it)
            }

        } else {
            managerUserLocation.requestPermissions(activity)
        }
    }

    /*fun checkUserLogged(onSuccess: (User?) -> Unit, onError: ExceptionClosure?) {
        // Chequea usuario logueado en UserAuthoritation
        /*Managers.managerUserAuthoritation!.isLogged(onSuccess: { [weak self] user in
            if user != nil && self!.oneTime {
                // Devolvemos el user logueado
                DispatchQueue.main.async {
                    onSuccess(user)
                }

            } else if user == nil && self!.oneTime {
                // Configuramos la escena inicial para usuario NO logueado
                self?.userNotLoggedIn(fromLogout: false)

                // Devolvemos nil
                DispatchQueue.main.async {
                    onSuccess(user)
                }
            }

        }) { error in
                if let retError = onError {
                    DispatchQueue.main.async {
                        retError(error)
                    }
                }
        }*/
    }*/


    /**
     * PRIVATE FUNCTIONS
     */

    /**
     * DELEGATE METHODS
     */
}