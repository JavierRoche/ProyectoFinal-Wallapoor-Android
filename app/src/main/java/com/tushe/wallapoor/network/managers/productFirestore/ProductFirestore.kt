@file:Suppress("NAME_SHADOWING")

package com.tushe.wallapoor.network.managers.productFirestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.network.models.Product

class ProductFirestore: ProductFirestoreManager {
    // Instancia para acceder al nodo principal de la DB de Firestore
    private var db = FirebaseFirestore.getInstance().collection("products")

    override fun selectProducts(onSuccess: (List<Product>) -> Unit, onError: ExceptionClosure) {
        // Realizamos la SELECT con Listener a Firebase.products de productos en venta ordenando por fecha
        db.orderBy("sentdate").whereEqualTo("state", 0).addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
            }
            snapshot?.let { snapshot ->
                val products: List<Product> = snapshot.documents.map { document ->
                    // Mapeamos cada QueryDocumentSnapshot a un objeto Product
                    Product.mapper(document = document as QueryDocumentSnapshot)
                }
                onSuccess(products)
            }
        }
    }

    override fun selectProductBySeller(userId: String, onSuccess: (List<Product>) -> Unit, onError: ExceptionClosure) {
        // Realizamos la SELECT sin Listener a Firebase.products del vendedor recibido por parametro ordenando por fecha
        db.orderBy("sentdate").whereEqualTo("seller", userId).get().addOnSuccessListener { snapshot ->
            val products: List<Product> = snapshot.documents.map { document ->
                // Mapeamos cada QueryDocumentSnapshot a un objeto Product
                Product.mapper(document = document as QueryDocumentSnapshot)
            }
            onSuccess(products)

        }.addOnFailureListener { error ->
            onError(error)
        }
    }

    override fun selectProductById(productId: String, onSuccess: (Product) -> Unit, onError: ExceptionClosure) {
        // Realizamos la SELECT sin Listener a Firebase.products del producto recibido
        db.whereEqualTo("productid", productId).get().addOnSuccessListener { snapshot ->
            if (snapshot.documents.isEmpty()) {
                return@addOnSuccessListener

            } else {
                // Mapeamos el QueryDocumentSnapshot al modelo
                onSuccess(Product.mapper(snapshot.first()))
            }

        }.addOnFailureListener { error ->
            onError(error)
        }
    }

    override fun insertProduct(product: Product, onSuccess: () -> Unit, onError: ExceptionClosure) {
        // Pasamos el producto al tipo QueryDocumentSnapshot
        val snapshot = Product.toSnapshot(product)

        // Insertamos el producto en Firestore BD
        db.add(snapshot).addOnSuccessListener {
            onSuccess()

        }.addOnFailureListener { error ->
            onError(error)
        }
    }

    override fun updateProduct(product: Product, onSuccess: () -> Unit, onError: ExceptionClosure) {
        // Primero intentamos recuperar el producto de BD
        db.whereEqualTo("productid", product.productId).get().addOnSuccessListener { snapshot ->
            // Pasamos el producto modificado al tipo QueryDocumentSnapshot
            val updatedSnapshot = Product.toSnapshot(product)

            // Actualizamos el producto en BD
            val document = snapshot.first()
            db.document(document.id).set(updatedSnapshot).addOnSuccessListener {
                onSuccess()

            }.addOnFailureListener { error ->
                onError(error)
            }

        }.addOnFailureListener { error ->
            onError(error)
        }
    }

    override fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: ExceptionClosure) {
        // Primero intentamos recuperar el producto de BD
        db.whereEqualTo("productid", product.productId).get().addOnSuccessListener { snapshot ->
            // Borramos el producto de BD
            val document = snapshot.first()
            db.document(document.id).delete().addOnSuccessListener {
                onSuccess()

            }.addOnFailureListener { error ->
                onError(error)
            }

        }.addOnFailureListener { error ->
            onError(error)
        }
    }
}