package com.example.adminblinkitclone.adminFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.adapter.AdapterOrders
import com.example.adminblinkitclone.databinding.FragmentOrderBinding
import com.example.adminblinkitclone.models.OrderedItems
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.launch


class OrderFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()
    private lateinit var adapterOrders: AdapterOrders
    private val binding: FragmentOrderBinding by lazy {
        FragmentOrderBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        getAllOrders()
        return binding.root
    }


    private fun getAllOrders() {

        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getAllOrders().collect { orderList ->
                if(orderList.isNotEmpty()){
                    val orderedList = ArrayList<OrderedItems>()
                    for(orders in orderList){
                        val title = StringBuilder()
                        var totalPrice = 0

                        for (products in orders.orderList!!) {
                            val price = products.productPrice?.substring(1)?.toInt()
                            val itemCount = products.productCount

                            totalPrice += (price!! * itemCount!!)

                            title.append("${products.productCategory}, ")
                        }


                        Log.d("Order Status", orders.orderStatus.toString())
                        val orderedItems = OrderedItems(orderId = orders.orderId, itemDate = orders.orderDate, itemStatus = orders.orderStatus, itemTitle = title.substring(0, title.length - 2).toString(), itemPrice = totalPrice, userAddress = orders.userAddress)
                        orderedList.add(orderedItems)
                    }
                    adapterOrders = AdapterOrders(requireContext(),::onOrderItemViewClicked)
                    binding.ordersRV.adapter = adapterOrders
                    adapterOrders.differ.submitList(orderedList)
                    binding.shimmerViewContainer.visibility = View.GONE

                }
            }
        }
    }

    private fun onOrderItemViewClicked(orderedItems: OrderedItems){

        val bundle = Bundle()
        bundle.putInt("status", orderedItems.itemStatus!! )
        bundle.putString("orderId", orderedItems.orderId)
        bundle.putString("userAddress",orderedItems.userAddress)

        findNavController().navigate(R.id.action_orderFragment_to_orderDetailFragment,bundle)
    }

}