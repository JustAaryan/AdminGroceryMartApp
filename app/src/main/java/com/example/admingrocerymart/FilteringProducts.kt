package com.example.admingrocerymart

import android.widget.Filter
import com.example.admingrocerymart.adapter.AdapterProduct
import com.example.admingrocerymart.models.Product
import java.util.Locale

class FilteringProducts(
    val adapter : AdapterProduct,
    val filter : ArrayList<Product>
) : Filter(){
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()
        if(!constraint.isNullOrEmpty()) {
            val filteredlist = ArrayList<Product>()
            val query = constraint.toString().trim().uppercase(Locale.getDefault()).split("")
            for (products in filter) {
                if (query.any {
                        products.Prodtitle?.uppercase(Locale.getDefault())?.contains(it) == true ||
                                products.Prodcat?.uppercase(Locale.getDefault())?.contains(it) == true ||
                                products.Prodprice?.toString()?.uppercase(Locale.getDefault())?.contains(it) == true ||
                                products.Prodtype?.uppercase(Locale.getDefault())?.contains(it) == true



                    }) {
                    filteredlist.add(products)
                }

            }
            result.values = filteredlist
            result.count  = filteredlist.size
        }


        else{
            result.values = filter
            result.count  = filter.size
        }
        return result
    }

    override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
        adapter.differ.submitList(result?.values as ArrayList<Product>)
    }

}