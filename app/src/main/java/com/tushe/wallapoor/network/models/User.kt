package com.tushe.wallapoor.network.models

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tushe.wallapoor.common.Sender
import java.io.Serializable

data class User (
    var sender: Sender,
    var email: String,
    var password: String? = null,
    var username: String = String(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var avatar: String = String(),
    var shopping: Int = 0,
    var sales: Int = 0
// Hacer Serializable la data class nos permite pasarla entre actividades
) : Serializable {

    constructor(id: String,
                email: String,
                username: String,
                latitude: Double,
                longitude: Double,
                avatar: String,
                shopping: Int,
                sales: Int) : this(Sender(id, email), email, String()) {
        this.username = username
        this.latitude = latitude
        this.longitude = longitude
        this.avatar = avatar
        this.shopping = shopping
        this.sales = sales
    }

    // Los metodos aqui incluidos estan declarados en un contexto estatico
    companion object {
        fun mapper(document: QueryDocumentSnapshot) : User {
            val json = document.data
            /// Extraemos los valores; como puede venir vacio indicamos un valor por defecto
            val userId = json["userid"] as? String ?: String()
            val email = json["email"] as? String ?: String()
            val username = json["username"] as? String ?: String()
            val latitude = json["latitude"] as? Double ?: 0.0
            val longitude = json["longitude"] as? Double ?: 0.0
            val avatar = json["avatar"] as? String ?: String()
            val shopping = json["shopping"] as? Long ?: 0
            val sales = json["sales"] as? Long ?: 0

            /// Creamos y devolvemos el objeto User
            return User(userId, email, username, latitude, longitude, avatar, shopping.toInt(), sales.toInt())
        }

        fun toSnapshot(user: User) : Map<String, Any> {
            // Creamos y devolvemos el objeto QueryDocumentSnapshot de Firestore
            return hashMapOf<String, Any>(
                "userid" to user.sender.id,
                "email" to user.email,
                "username" to user.username,
                "latitude" to user.latitude,
                "longitude" to user.longitude,
                "avatar" to user.avatar,
                "shopping" to user.shopping,
                "sales" to user.sales
            )
        }
    }
}