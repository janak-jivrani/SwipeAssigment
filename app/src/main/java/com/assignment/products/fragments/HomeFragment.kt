package com.assignment.products.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.assignment.products.R
import com.assignment.products.adapters.ProductsListAdapter
import com.assignment.products.core.CommonUtils
import com.assignment.products.core.CommonUtils.isInternetAvailable
import com.assignment.products.core.ViewModelFactory
import com.assignment.products.core.getViewModelFromFactory
import com.assignment.products.core.showToast
import com.assignment.products.databinding.FragmentHomeBinding
import com.assignment.products.di.ProductsApplication
import com.assignment.products.models.ProductsListData
import com.assignment.products.viewmodels.ProductsState
import com.assignment.products.viewmodels.ProductsViewModel
import javax.inject.Inject

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding

    private val productsList: ArrayList<ProductsListData?> = arrayListOf()

    private var mProductsListAdapter: ProductsListAdapter? = null

    @Inject
    internal lateinit var vmFactory: ViewModelFactory<ProductsViewModel>
    private lateinit var viewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProductsApplication.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModelFromFactory(vmFactory)

        setUpClicks(
            binding.imgBtnSearch,
            binding.imgBtnCart,
            binding.imgBtnAddProduct,
            binding.imgCloseSearch
        )
        initProductList()
        setObserver()

        if (isInternetAvailable(mActivity!!)) {
            viewModel.getProductsList()
        }
    }

    private fun initProductList() {
        mProductsListAdapter = ProductsListAdapter(mActivity!!, productsList) { item, pos ->

        }
        binding.recyclerProducts.apply {
            layoutManager = GridLayoutManager(mActivity, 2)
            adapter = mProductsListAdapter
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })
    }

    private fun setObserver() {
        viewModel.liveDataObserver.observe(viewLifecycleOwner) {
            when (it) {
                is ProductsState.Loading -> {
                    if (it.isLoading) {
                        mDialog.show()
                    } else {
                        mDialog.dismiss()
                    }
                }

                is ProductsState.Success -> {
                    productsList.clear()
                    productsList.addAll(it.productsList!!)

                    mProductsListAdapter?.updateList(it.productsList)
                }

                is ProductsState.Error -> {
                    showToast(it.error)
                }

                else -> {}
            }
        }
    }

    fun filter(text: String) {
        val temp: ArrayList<ProductsListData?> = ArrayList()
        for (d in productsList) {
            if (d?.productName!!.lowercase()
                    .contains(text.lowercase()) || (d.productType!!.lowercase()
                    .contains(text.lowercase()))
            ) {
                temp.add(d)
            }
        }
        mProductsListAdapter?.updateList(temp)
    }

    private fun animationSearchView() {
        if (binding.mcvSearch.visibility == View.GONE) {
            binding.mcvSearch.visibility = View.VISIBLE
            binding.mcvSearch.alpha = 0f
            binding.mcvSearch.animate().alpha(1f).setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        binding.mcvSearch.visibility = View.VISIBLE
                    }
                })
        } else {
            binding.mcvSearch.animate().alpha(0f).setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        binding.mcvSearch.visibility = View.GONE
                    }
                })

            CommonUtils.hideKeyBoard(binding.root)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.imgBtnSearch -> {
                animationSearchView()
            }

            R.id.imgBtnCart -> {
                showToast("Cart")
            }

            R.id.imgBtnAddProduct -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddProductFragment())
            }

            R.id.imgCloseSearch -> {
                binding.edtSearch.setText("")
                binding.imgBtnSearch.performClick()
            }
        }
    }
}