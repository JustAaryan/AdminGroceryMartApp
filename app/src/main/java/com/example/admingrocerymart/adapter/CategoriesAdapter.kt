package com.example.admingrocerymart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admingrocerymart.databinding.ItemViewProdcatBinding
import com.example.admingrocerymart.models.Categories

class CategoriesAdapter(
    private val catelogueArrayList: ArrayList<Categories>,
   val oncategoryclicked: (Categories) -> Unit,

    ) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(){
    class CategoriesViewHolder(val binding:ItemViewProdcatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(ItemViewProdcatBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return catelogueArrayList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = catelogueArrayList[position]
        holder.binding.apply {
            productcatimg.setImageResource(category.Icon)
            producttitle.text= category.category
        }
        holder.itemView.setOnClickListener{
            oncategoryclicked(category)

        }

    }


}
