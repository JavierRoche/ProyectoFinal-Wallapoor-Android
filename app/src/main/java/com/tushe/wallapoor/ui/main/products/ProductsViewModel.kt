@file:Suppress("NAME_SHADOWING")

package com.tushe.wallapoor.ui.main.products

import android.content.Context
import android.location.Location
import com.tushe.wallapoor.common.Category
import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.common.Filter
import com.tushe.wallapoor.network.managers.Managers
import com.tushe.wallapoor.network.managers.productFirestore.ProductFirestore
import com.tushe.wallapoor.network.managers.userFirestore.UserFirestore
import com.tushe.wallapoor.network.models.Product
import com.tushe.wallapoor.network.models.User

class ProductsViewModel(private val delegate: ProductsViewModelDelegate) {
    // Delegado de comunicacion con el fragmento
    //private var delegate: ProductsViewModelDelegate? = null

    /**
     * PROTOCOLS
     **/

    // Protocolo con los metodos abstractos del fragmento que recogen las interaciones del usuario y que seran sobreescritos en la actividad
    interface ProductsViewModelDelegate {
        fun productsModelCreated()
        fun filterApplied()
    }
    
    
    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        // Usuario logueado en la App
        lateinit var user: User
        // Lista original con todos los productos descargados de Firebase DB
        private var originalProductList: List<Product> = emptyList()
        // Es la foto tras volver de la scena de seleccion de filtros
        private var auxiliarProductList: List<Product> = emptyList()
        // Es la lista de la que tira el Grid principal
        private var actualProductList: List<Product> = emptyList()
        // Dos referencias para almacenar el filtro original y el actual
        private var originalFilter: Filter = Filter()
        private var actualFilter: Filter = Filter()
    }

    /*init {
        // Comprobamos que el contexto de la actividad implementa el protocolo
        if (context is ProductsViewModelDelegate)
            delegate = context
        else
            throw IllegalArgumentException("Context doesn't implement ${ProductsViewModelDelegate::class.java.canonicalName}")
    }*/


    /**
     * PUBLIC FUNCTIONS
     **/

    fun viewWasCreated() {
        Managers.managerProductFirestore = ProductFirestore()
        Managers.managerProductFirestore?.selectProducts({ products ->
            // Aplicamos el filtro de distancia a la lista original descargada
            this.filterByDistance(products) { products ->
                originalProductList = products
                this.updateSituation(true, products)
            }

        }, { error ->
            println(error.localizedMessage)
        })
    }

    fun getProductList() : List<Product> {
        return actualProductList
    }


    /**
     * PRIVATE FUNCTIONS
     **/

    private fun filterByCategory(products: List<Product>, category: Category) : List<Product> {
        val filteredProducts: MutableList<Product> = mutableListOf()
        products.map { product ->
            if (product.category.ordinal != category.ordinal) {
                filteredProducts.add(product)
            }
        }
        return filteredProducts
    }

    private fun filterByDistance(products: List<Product>, toDistance: Double = 50000.0, onSuccess: (List<Product>) -> Unit) {
        // Obtenemos la localizacion del usuario logueado
        val userLocation = Location(String())
        userLocation.latitude = user.latitude
        userLocation.longitude = user.longitude

        // Inicializamos valores para el algoritmo que buscara por cada usuario si esta en rango con el user logueado
        val filteredProducts: MutableList<Product> = mutableListOf()
        var count = 1

        for (product in products) {
            this.getSellerData(product, { user ->
                if (user != null) {
                    // Con la location del dueño del producto calculamos la distancia al usuario en metros y linea recta
                    val productLocation = Location(String())
                    productLocation.latitude = user.latitude
                    productLocation.longitude = user.longitude

                    val distance = productLocation.distanceTo(userLocation)
                    if (distance <= toDistance)
                        filteredProducts.add(product)

                    if (count == products.size)
                        onSuccess(filteredProducts)

                    count += 1
                }
            }, {}) // El producto se ignora si ha habido error
        }
    }

    private fun getSellerData(product: Product, onSuccess: (User?) -> Unit, onError: ExceptionClosure?) {
        Managers.managerUserFirestore = UserFirestore()
        Managers.managerUserFirestore?.selectUser(product.seller, { firestoreUser ->
            if (firestoreUser != null)
                onSuccess(firestoreUser)

        }, { error ->
            if (onError != null)
                onError(error)
        })
    }

    private fun updateSituation(initialSituation: Boolean, products: List<Product>, filter: Filter = Filter()) {
        // Guardamos la foto en la lista auxiliar
        auxiliarProductList = products
        // Guardamos la foto en la lista principal de la que tira el Grid
        actualProductList = products
        // Actualizamos o inicializamos el filtro segun si viene informado
        actualFilter = filter

        /// Avisamos al controlador de que el modelo de datos se ha creado
        if (initialSituation) {
            originalFilter = filter
            delegate.productsModelCreated()

        } else {
            delegate.filterApplied()
        }
    }
}