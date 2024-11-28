package com.example.admingrocerymart.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.admingrocerymart.databinding.ItemViewImgSelectionBinding

class Adapterselectedimg(val imageuris:ArrayList<Uri>):RecyclerView.Adapter<Adapterselectedimg.selectedimgholdr> (){
    class selectedimgholdr (val binding: ItemViewImgSelectionBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): selectedimgholdr {
        return selectedimgholdr(ItemViewImgSelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return imageuris.size
    }

    override fun onBindViewHolder(holder: selectedimgholdr, position: Int) {
      val img=imageuris[position]
       holder.binding.apply {
ivimage.setImageURI(img)
       }
        holder.binding.closebtn.setOnClickListener{
            if (position<imageuris.size){
                imageuris.removeAt(position)
                notifyItemRemoved(position)
            }

        }
    }


}