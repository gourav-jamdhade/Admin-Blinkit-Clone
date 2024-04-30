package com.example.adminblinkitclone.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminblinkitclone.databinding.ItemViewProductCategoriesBinding
import com.example.adminblinkitclone.models.Category

class CategoriesAdapter(
    val categoryList: ArrayList<Category>, val onCategoryClicked: (Category) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {


    class CategoryViewHolder(val binding: ItemViewProductCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemViewProductCategoriesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = categoryList[position]
        Log.d("CategoriesAdapter", "Binding item at position $position")
        holder.binding.apply {

            ivCategoryImage.setImageResource(category.icon)
            tvCategoryTitle.text = category.category
        }

        holder.itemView.setOnClickListener {
            Log.d("CategoriesAdapter", "Item clicked at position $position")
            onCategoryClicked(category)
        }
    }
}