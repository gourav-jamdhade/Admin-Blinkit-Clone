package com.example.adminblinkitclone.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adminblinkitclone.databinding.AddProductRvBinding
import com.example.adminblinkitclone.databinding.ShowListBinding

class AdapterSelectedImage(var imageUris : ArrayList<Uri>):RecyclerView.Adapter<AdapterSelectedImage.SelectedImageViewHolder>() {

    class SelectedImageViewHolder(val binding:AddProductRvBinding) : ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        return SelectedImageViewHolder(AddProductRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {

        val image = imageUris[position]
        holder.binding.ivImage.setImageURI(image)

        holder.binding.closeBtn.setOnClickListener {
            // Remove the item from the list
            imageUris.removeAt(holder.adapterPosition)
            // Notify the adapter about the item removal
            notifyItemRemoved(holder.adapterPosition)
        }
    }
}