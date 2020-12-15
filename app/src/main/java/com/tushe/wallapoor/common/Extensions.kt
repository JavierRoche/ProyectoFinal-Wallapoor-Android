package com.tushe.wallapoor.common

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable
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

typealias ExceptionClosure = (Exception) -> Unit

data class Sender(
        var id: String,
        var displayName: String) : Serializable

data class Filter(
        var motor: Boolean = true,
        var textile: Boolean = true,
        var homes: Boolean = true,
        var informatic: Boolean = true,
        var sports: Boolean = true,
        var services: Boolean = true,
        var distance: Double = 50.0,
        var text: String = String())

enum class Category(rawValue: Int) : Serializable {
    MOTOR(0) { override fun toString(): String { return "Engine and accessories" } },
    TEXTILE(1) { override fun toString(): String { return "Textile" } },
    HOMES(2) { override fun toString(): String { return "Home" } },
    INFORMATIC(3) { override fun toString(): String { return "Computer and electronic" } },
    SPORTS(4) { override fun toString(): String { return "Sport and leisure" } },
    SERVICES(5) { override fun toString(): String { return "Services" } }
}

enum class ProductState(state: Int) : Serializable {
    SELLING(0),
    SOLD(1)
}