package com.example.adminblinkitclone.adminFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.Utils
import com.example.adminblinkitclone.adapter.CartProductsAdapter
import com.example.adminblinkitclone.databinding.FragmentOrderDetailBinding
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.launch


class OrderDetailFragment : Fragment() {


    private val binding: FragmentOrderDetailBinding by lazy {
        FragmentOrderDetailBinding.inflate(layoutInflater)
    }

    private var status = 0
    private val viewModel: AdminViewModel by viewModels()
    private var orderId = ""
    private var currentStatus = 0
    private lateinit var adapter: CartProductsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getValues()
        onBackPressed()
        settingStatus(status)
        onChangeStatusButtonClicked()
        lifecycleScope.launch {
            getOrderedProducts()
        }
        return binding.root
    }

    private fun onChangeStatusButtonClicked() {
        binding.btnChangeStatus.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)

            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.menuOrdered->{
                        currentStatus = 0
                        if (currentStatus > status){
                            status = 0
                            settingStatus(0)
                            viewModel.updateOrderStatus(orderId,0)

                        }else{
                            Utils.showToast(requireContext(),"Order is already ordered")
                        }

                        true

                    }
                    R.id.menuReceived -> {
                        currentStatus = 1
                        if (currentStatus > status){
                            status = 1
                            settingStatus(1)
                            viewModel.updateOrderStatus(orderId,1)

                        }else{
                            Utils.showToast(requireContext(),"Order is already received")
                        }

                        true
                    }


                    R.id.menuDispatched -> {
                        currentStatus = 2
                        if (currentStatus > status){
                            status = 2
                            settingStatus(2)
                            viewModel.updateOrderStatus(orderId,2)

                        }else{
                            Utils.showToast(requireContext(),"Order is already dispatched")
                        }

                        true
                    }

                    R.id.menuDelivered -> {
                        currentStatus = 3
                        if (currentStatus > status){
                            status = 3
                            settingStatus(3)
                            viewModel.updateOrderStatus(orderId,3)

                        }

                        true
                    }


                    else -> {
                        false
                    }
                }

            }

        }
    }


    private fun onBackPressed() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_orderFragment)
        }
    }

    private suspend fun getOrderedProducts() {

        viewModel.getOrderedProducts(orderId).collect { cartList ->

            adapter = CartProductsAdapter()
            binding.rvProductsItems.adapter = adapter
            adapter.differ.submitList(cartList)
        }

    }

    private fun settingStatus(status: Int) {
        when (status) {
            0 -> {
                setColor(
                    R.color.blue,
                    binding.ivOrderDone
                )
            }

            in 1..3 -> {
                setColor(
                    R.color.blue,
                    binding.ivOrderDone,
                    binding.statusOrderReceived,
                    binding.ivOrderReceived
                )
                if (status >= 2) {
                    setColor(
                        R.color.blue,
                        binding.statusOrderDispatched,
                        binding.ivOrderDispatched
                    )
                    if (status >= 3) {
                        setColor(
                            R.color.blue,
                            binding.statusOrderDelivered,
                            binding.ivOrderDelivered
                        )
                    }
                }
            }
        }


    }

    private fun setColor(colorId: Int, vararg views: View) {
        for (view in views) {
            view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), colorId)
        }
    }

    private fun getValues() {
        val bundle = arguments
        status = bundle?.getInt("status")!!
        orderId = bundle.getString("orderId")!!

        binding.tvShowAddress.text = bundle.getString("userAddress").toString()
        Log.d("Status", status.toString())

    }

}