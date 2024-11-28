package com.example.admingrocerymart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.denzcoskun.imageslider.models.SlideModel
import com.example.admingrocerymart.FilteringProducts
import com.example.admingrocerymart.databinding.ItemViewprodBinding
import com.example.admingrocerymart.models.Product

class AdapterProduct(
    val oneditbtnclicked: (Product) -> Unit) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() ,Filterable{
    class ProductViewHolder (val binding: ItemViewprodBinding) : ViewHolder(binding.root) {

    }

    val diffutil = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.prod_id == newItem.prod_id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemViewprodBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()
            val prodImage = product.Prodimageuris
            for(i in 0 until  prodImage?.size!!){
                imageList.add(SlideModel(product.Prodimageuris!!.get(i).toString()))
            }
            imageslider.setImageList(imageList)
            tvprodtitle.text = product.Prodtitle
            val quantityprod = product.Prodqty.toString() + product.Produnit
            tvprodqty.text = quantityprod
            tvprodprice.text = "Rs"+product.Prodprice
        }
        holder.itemView.setOnClickListener{
            oneditbtnclicked(product)
        }

    }
    val filter : FilteringProducts? = null
    var originallist = ArrayList<Product>()
    override fun getFilter(): Filter {
        if(filter==null) return FilteringProducts(this,originallist)
        return filter

    }


}