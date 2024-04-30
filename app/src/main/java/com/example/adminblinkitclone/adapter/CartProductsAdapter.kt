package com.example.adminblinkitclone.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.adminblinkitclone.FilteringProducts
import com.example.adminblinkitclone.dataClasses.Product
import com.example.adminblinkitclone.databinding.CartLayoutItemViewBinding
import com.example.userblinkitclone.roomdb.CartProducts

class CartProductsAdapter : RecyclerView.Adapter<CartProductsAdapter.CartProductsViewHolder>() {

    class CartProductsViewHolder(val binding: CartLayoutItemViewBinding) :
        ViewHolder(binding.root) {}


    val filter: FilteringProducts? = null
    var originalList = ArrayList<Product>()
    private val diffUtil = object : DiffUtil.ItemCallback<CartProducts>() {
        override fun areItemsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            Log.d("DiffUtil", (oldItem.productId == newItem.productId).toString())
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {

        return CartProductsViewHolder(
            CartLayoutItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {

        val product = differ.currentList[position]

        holder.binding.apply {
            Glide.with(holder.itemView).load(product.productImage).into(ivImageCart)
            tvProductNameCart.text = product.productTitle
            tvProductQuantityCart.text = product.productQuantity
            tvProductPriceCart.text = product.productPrice
            productCountCart.text = product.productCount.toString()



        }




    }


}