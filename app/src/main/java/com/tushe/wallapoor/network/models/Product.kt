package com.tushe.wallapoor.network.models

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tushe.wallapoor.common.Category
import com.tushe.wallapoor.common.ProductState
import java.io.Serializable
import java.sql.Timestamp
import java.util.*

data class Product (
        var productId: String = UUID.randomUUID().toString(),
        var seller: String,
        var state: ProductState = ProductState.SELLING,
        var title: String,
        var category: Category,
        var description: String,
        var price: Int,
        var viewers: Int = 0,
        var sentDate: Date = Date(),
        var photos: List<String>,
        var heightMainphoto: Double = 0.0
// Hacer Serializable la data class nos permite pasarla entre actividades
) : Serializable {

    constructor(productId: String,
                seller: String,
                state: Int,
                title: String,
                category: Int,
                description: String,
                price: Int,
                viewers: Int,
                sentDate: Date,
                photos: List<String>,
                heightMainphoto: Double) : this(seller = seller, title = title, category = Category.values()[category],
                                                description = description, price = price, photos = photos) {
        this.productId = productId
        this.state = ProductState.values()[state]
        this.viewers = viewers
        this.sentDate = sentDate
        this.heightMainphoto = heightMainphoto
    }

    // Los metodos aqui incluidos estan declarados en un contexto estatico
    companion object {
        fun mapper(document: QueryDocumentSnapshot) : Product {
            val json = document.data
            // Extraemos los valores; como puede venir vacio indicamos un valor por defecto
            val productId = json["productid"] as? String ?: String()
            val seller = json["seller"] as? String ?: String()
            val state = json["state"] as? Long ?: 0 // Estado Selling si no puede mapear
            val title = json["title"] as? String ?: String()
            val category = json["category"] as? Long ?: 2 /// Categoria Hogar si no puede mapear
            val description = json["description"] as? String ?: String()
            val price = json["price"] as? Long ?: 0
            val viewers = json["viewers"] as? Long ?: 0
            val sentDate = json["sentdate"] as? com.google.firebase.Timestamp ?: com.google.firebase.Timestamp.now()
            val heightMainphoto = json["heightmainphoto"] as? Double ?: 0.0
            val photo2 = json["photo2"] as? String ?: String()
            val photo3 = json["photo3"] as? String ?: String()
            val photo4 = json["photo4"] as? String ?: String()

            val photos: MutableList<String> = mutableListOf()
            photos.add(json["photo1"] as? String ?: String())
            if (photo2.isNotEmpty()) {
                photos.add(photo2)
            }
            if (photo3.isNotEmpty()) {
                photos.add(photo3)
            }
            if (photo4.isNotEmpty()) {
                photos.add(photo4)
            }

            // Creamos y devolvemos el objeto Product
            return Product(productId, seller, state.toInt(), title, category.toInt(), description, price.toInt(), viewers.toInt(), sentDate.toDate(), photos, heightMainphoto)
        }

        fun toSnapshot(product: Product) : Map<String, Any> {
            // Creamos y devolvemos el objeto QueryDocumentSnapshot de Firestore
            return hashMapOf<String, Any>(
                "productid" to product.productId,
                "seller" to product.seller,
                "state" to product.state.ordinal,
                "title" to product.title,
                "category" to product.category.ordinal,
                "description" to product.description,
                "price" to product.price,
                "viewers" to product.viewers,
                "sentdate" to product.sentDate,
                "heightmainphoto" to product.heightMainphoto,
                "photo1" to product.photos[0],

                when (product.photos.size) {
                    1 -> {
                        "photo2" to String()
                        "photo3" to String()
                        "photo4" to String()
                    }
                    2 -> {
                        "photo2" to product.photos[1]
                        "photo3" to String()
                        "photo4" to String()
                    }
                    3 -> {
                        "photo2" to product.photos[1]
                        "photo3" to product.photos[2]
                        "photo4" to String()
                    }
                    4 -> {
                        "photo2" to product.photos[1]
                        "photo3" to product.photos[2]
                        "photo4" to product.photos[3]
                    }
                    else -> {
                        "photo2" to String()
                        "photo3" to String()
                        "photo4" to String()
                    }
                })
        }
    }
}
