package com.tushe.wallapoor.network.managers.productFirestore

import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.network.models.Product

interface ProductFirestoreManager {
    fun selectProducts(onSuccess: (List<Product>) -> Unit, onError: ExceptionClosure)
    fun selectProductBySeller(userId: String, onSuccess: (List<Product>) -> Unit, onError: ExceptionClosure)
    fun selectProductById(productId: String, onSuccess: (Product) -> Unit, onError: ExceptionClosure)
    fun insertProduct(product: Product, onSuccess: () -> Unit, onError: ExceptionClosure)
    fun updateProduct(product: Product, onSuccess: () -> Unit, onError: ExceptionClosure)
    fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: ExceptionClosure)
}