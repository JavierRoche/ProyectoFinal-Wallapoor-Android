package com.tushe.wallapoor.ui.main.products

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tushe.wallapoor.R
import com.tushe.wallapoor.common.inflate
import com.tushe.wallapoor.network.models.Product
import kotlinx.android.synthetic.main.product_cell.view.*
import java.lang.IllegalArgumentException
import java.util.*

class ProductsAdapter(private val context: Context,
                      private val delegate: TapDelegate,
                      private var productList: List<Product>) : BaseAdapter() {
    /**
     * PROTOCOL
     */

    interface TapDelegate {
        fun onItemTap(product: Product?)
    }


    /**
     * LIFE CYCLE
     */

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = this.productList[position]
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cell = inflator.inflate(R.layout.product_cell, null)

        // Fijamos la informacion del producto
        cell.priceTextView.text = product.price.toString()

        // Fijamos la imagen recuperada en formato url con Glide
        Glide.with(context)
                .load(product.photos[0])
                .apply(RequestOptions().placeholder((R.mipmap.ic_launcher)))
                .into(cell.photoImageView)

        cell.setOnClickListener {
            delegate.onItemTap(product)
        }

        return cell
    }


    /**
     * PUBLIC FUNCTIONS
     */

    // Metodo que repinta el modelo
    fun setChanges(productList: List<Product>) {
        this.productList = productList
        notifyDataSetChanged()
    }
}