package com.example.adminblinkitclone.adminFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.adminblinkitclone.Constants
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.Utils
import com.example.adminblinkitclone.adapter.AdapterProduct
import com.example.adminblinkitclone.adapter.CategoriesAdapter
import com.example.adminblinkitclone.dataClasses.Product
import com.example.adminblinkitclone.databinding.EditProductLayoutBinding
import com.example.adminblinkitclone.databinding.FragmentHomeBinding
import com.example.adminblinkitclone.models.Category
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()

    private lateinit var adapterProduct: AdapterProduct
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setCategories()

        searchProducts()
        getAllTheProducts("All")
        return binding.root
    }

    private fun searchProducts() {
        binding.searchEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val query = s.toString().trim()
                adapterProduct.getFilter().filter(query)

            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onCategoryClicked(category: Category) {

        getAllTheProducts(category.category)
    }

    private fun getAllTheProducts(category: String) {

        binding.shimmerViewCategories.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllTheProducts(category).collect {

                if (it.isEmpty()) {
                    binding.tvText.visibility = View.VISIBLE
                    binding.rvProducts.visibility = View.GONE
                } else {
                    binding.tvText.visibility = View.GONE
                    binding.rvProducts.visibility = View.VISIBLE
                }

                adapterProduct = AdapterProduct(::onEditButtonClicked)
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)

                adapterProduct.originalList = it as ArrayList<Product>
                binding.shimmerViewCategories.visibility = View.GONE

            }
        }

    }

    private fun setCategories() {
        val categoryList = ArrayList<Category>()

        for (i in 0 until Constants.allProductsCategory.size) {
            categoryList.add(
                Category(
                    Constants.allProductsCategory[i],
                    Constants.allProductCategoryIcon[i]
                )
            )

        }



        binding.rvCategories.adapter = CategoriesAdapter(categoryList, ::onCategoryClicked)

    }

    private fun onEditButtonClicked(product: Product) {


        val editProduct = EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            etProductTitle.setText(product.productTitle)
            etQuantity.setText(product.productQuantity!!.toString())
            etProductUnit.setText(product.productUnit.toString())
            etProductType.setText(product.productType)
            etProductPrice.setText(product.productPrice!!.toString())
            etProductStock.setText(product.productStock!!.toString())
            etProductCategory.setText(product.productCategory)
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(editProduct.root)
            .create()

        alertDialog.show()

        editProduct.btnEditProduct.setOnClickListener {
            editProduct.apply {
                etProductTitle.isEnabled = true
                etQuantity.isEnabled = true
                etProductUnit.isEnabled = true
                etProductType.isEnabled = true
                etProductPrice.isEnabled = true
                etProductStock.isEnabled = true
                etProductCategory.isEnabled = true
            }
        }
        setAutoCompleteTextViews(editProduct)

        editProduct.btnSaveProduct.setOnClickListener {

            lifecycleScope.launch {
                editProduct.apply {
                    product.productTitle=etProductTitle.text.toString()
                    product.productPrice=etProductPrice.text.toString().toInt()
                    product.productUnit=etProductUnit.text.toString()
                    product.productQuantity=etQuantity.text.toString().toInt()
                    product.productCategory=etProductCategory.text.toString()
                    product.productStock=etProductStock.text.toString().toInt()
                    product.productType=etProductType.text.toString()
                    viewModel.savingUpdatedProduct(product)


                }

                Utils.showToast(requireContext(),"Changes Saved")
                alertDialog.dismiss()
            }

        }
    }

    private fun setAutoCompleteTextViews(editProduct: EditProductLayoutBinding) {

        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category =
            ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductsCategory)
        val types = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductTypes)

        editProduct.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(types)
        }
    }
}