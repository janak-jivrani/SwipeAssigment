package com.assignment.products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.assignment.products.R
import com.assignment.products.core.CommonUtils
import com.assignment.products.databinding.RvProductsItemBinding
import com.assignment.products.models.ProductsListData
import com.bumptech.glide.Glide

class ProductsListAdapter(
    private val context: Context,
    private var arrayList: ArrayList<ProductsListData?>,
    val action: (item: ProductsListData, pos: Int) -> Unit
) : RecyclerView.Adapter<ProductsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvProductsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    fun updateList(list: ArrayList<ProductsListData?>) {
        arrayList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val bindingItem: RvProductsItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(item: ProductsListData?) {
            Glide.with(context).load(item?.image).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_product_error).into(bindingItem.imgProduct)

            bindingItem.txtProductType.text = item?.productType
            bindingItem.txtProductName.text = item?.productName
            bindingItem.txtProductPrice.text = context.resources.getString(
                R.string.dollar_price, CommonUtils.currencyFormatter(item?.price)
            )
            bindingItem.txtProductTax.text = context.resources.getString(
                R.string.tax, CommonUtils.currencyFormatter(item?.tax)
            )

            bindingItem.imgAddToCart.setOnClickListener {
                Toast.makeText(context, "Add to cart.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}