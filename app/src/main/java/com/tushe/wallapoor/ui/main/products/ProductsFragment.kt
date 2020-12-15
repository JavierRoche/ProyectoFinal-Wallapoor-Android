package com.tushe.wallapoor.ui.main.products

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.inflate
import com.tushe.wallapoor.network.models.Product
import com.tushe.wallapoor.network.models.User
import kotlinx.android.synthetic.main.products_fragment.*

class ProductsFragment: Fragment(),
        ProductsAdapter.TapDelegate,
        ProductsViewModel.ProductsViewModelDelegate {
    // Delegado que maneja los eventos de usuario sobre el fragmento
    private var delegate: ProductsFragmentDelegate? = null
    // Para usar el adaptador y sus layouts
    private lateinit var adapter: ProductsAdapter

    /**
     * PROTOCOLS
     */

    // Protocolo delegado
    interface ProductsFragmentDelegate {
        fun onCreateProduct()
    }


    /**
     * STATIC FUNCTIONS
     **/

    companion object {
        // ViewModel
        lateinit var viewModel: ProductsViewModel

        // Al hacer apply podemos incluirle argumentos en su interior
        fun newInstance(context: Context, user: User) = ProductsFragment().apply {
            viewModel = ProductsViewModel(this)
            ProductsViewModel.user = user
        }
    }


    /**
     * LIFE CYCLE
     */

    // Liga el fragmento a la actividad
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Comprobamos que el contexto de la actividad implementa el protocolo
        if (context is ProductsFragmentDelegate)
            delegate = context
        else
            throw IllegalArgumentException("Context doesn't implement ${ProductsFragmentDelegate::class.java.canonicalName}")
    }

    // Una vez creada la instancia del fragmento
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.products_fragment)
    }

    // Marca la disponibilidad de la vista tras su creacion
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lanzamos la creacion de un nuevo producto
        newProductButton.setOnClickListener { delegate?.onCreateProduct() }
    }

    override fun onResume() {
        super.onResume()

        // Recuperamos los productos de Firestore
        viewModel.viewWasCreated()
    }


    /**
     * DELEGATE METHODS
     **/

    override fun productsModelCreated() {
        activity?.applicationContext?.let { context ->
            // Construimos el adaptador con el contexto, el listener de eventos y la lista de productos
            adapter = ProductsAdapter(context, this, viewModel.getProductList())
            // Asignamos al grid el adaptador creado y el layout
            productsGridView.adapter = adapter
        }
    }

    override fun filterApplied() {
        println("filterApplied")
    }

    override fun onItemTap(product: Product?) {
        println("onItemTap")
    }
}