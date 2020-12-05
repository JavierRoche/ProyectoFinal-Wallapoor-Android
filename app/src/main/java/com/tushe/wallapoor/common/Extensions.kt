package com.tushe.wallapoor.common

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

// Las extensiones nos permiten añadir metodos a las clases sin tener que tocar el fuente de la clase

// Funcion inline que podemos invocar desde cualquier actividad
fun isFirstTimeCreated(savedInstanceState: Bundle?) : Boolean
        = savedInstanceState == null

// Funcion inline que extiende el container del ViewGroup
fun ViewGroup.inflate(idLayout: Int, attachToRoot: Boolean = false): View
        = LayoutInflater.from(this.context).inflate(idLayout, this, attachToRoot)

fun showSnackbar(activity: Activity, stringId: Int, actionId: Int, listener: View.OnClickListener) {
    Snackbar.make(
        activity.findViewById(android.R.id.content),
        activity.getString(stringId),
        Snackbar.LENGTH_INDEFINITE).setAction(activity.getString(actionId), listener).show()
}

typealias ExceptionClosure = (Exception) -> Void