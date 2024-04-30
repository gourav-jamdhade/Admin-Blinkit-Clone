package com.example.adminblinkitclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.databinding.OrderFragmentItemViewBinding
import com.example.adminblinkitclone.models.OrderedItems


class AdapterOrders(private val context: Context, val onOrderItemViewClicked: (OrderedItems) -> Unit) : RecyclerView.Adapter<AdapterOrders.OrdersViewHolder>() {
    class OrdersViewHolder(val binding: OrderFragmentItemViewBinding) : ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<OrderedItems>() {
        override fun areItemsTheSame(oldItem: OrderedItems, newItem: OrderedItems): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: OrderedItems, newItem: OrderedItems): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {

        return OrdersViewHolder(
            OrderFragmentItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.binding.apply {
            orderTitle.text = order.itemTitle
            orderDate.text = order.itemDate
            orderPrice.text = "₹${order.itemPrice.toString()}"

            when (order.itemStatus) {
                0 -> {
                    orderStatus.text = "Ordered"
                    orderStatus.backgroundTintList=ContextCompat.getColorStateList(context, R.color.yellow)
                }

                1 -> {
                    orderStatus.text = "Recieved"
                    orderStatus.backgroundTintList=ContextCompat.getColorStateList(context, R.color.blue)
                }

                2 -> {
                    orderStatus.text = "Dispatched"
                    orderStatus.backgroundTintList=ContextCompat.getColorStateList(context, R.color.green)
                }

                3 -> {
                    orderStatus.text = "Delivered"
                    orderStatus.backgroundTintList=ContextCompat.getColorStateList(context, R.color.orange)
                }


            }
        }

        holder.itemView.setOnClickListener {
            onOrderItemViewClicked(order)
        }
    }
}