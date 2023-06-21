package com.assignment.products.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.assignment.products.R
import com.assignment.products.core.CommonUtils.isInternetAvailable
import com.assignment.products.core.ViewModelFactory
import com.assignment.products.core.getPathFromInputStreamUri
import com.assignment.products.core.getViewModelFromFactory
import com.assignment.products.core.showToast
import com.assignment.products.databinding.FragmentAddProductBinding
import com.assignment.products.di.ProductsApplication
import com.assignment.products.models.AddProductRequest
import com.assignment.products.viewmodels.ProductsState
import com.assignment.products.viewmodels.ProductsViewModel
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import javax.inject.Inject

class AddProductFragment : BaseFragment() {
    private lateinit var binding: FragmentAddProductBinding

    private var mProductImagePath: String = ""

    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModelFromFactory(vmFactory)

        setUpClicks(
            binding.imgBtnBack, binding.btnAddProduct, binding.txtUploadPhoto
        )
        setObserver()
        if (isInternetAvailable(mActivity!!)) {
            viewModel.getProductsList()
        }
        initView()
    }

    private fun initView() {
        val items = listOf(
            "Electronics", "Cloths", "Snacks", "Fashion", "Computers", "Shoes", "Kitchen appliances"
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.textFieldType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
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

                is ProductsState.SuccessAddProduct -> {
                    mDialog.dismiss()
                    showToast(it.productRes?.message.toString())

                    findNavController().navigateUp()
                }

                is ProductsState.Error -> {
                    showToast(it.error)
                }

                else -> {}
            }
        }
    }

    private fun isValid(): Boolean {
        if (binding.edtProductType.text.toString().trim().isEmpty()) {
            showToast(mActivity!!.getString(R.string.err_prod_type))
            return false
        } else if (binding.edtProductName.text.toString().trim().isEmpty()) {
            showToast(mActivity!!.getString(R.string.err_prod_name))
            return false
        } else if (binding.edtProductPrice.text.toString().trim().isEmpty()) {
            showToast(mActivity!!.getString(R.string.err_prod_price))
            return false
        } else if (binding.edtProductTax.text.toString().trim().isEmpty()) {
            showToast(mActivity!!.getString(R.string.err_prod_tax))
            return false
        }
        return true
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.imgBtnBack -> {
                findNavController().navigateUp()
            }

            R.id.txtUploadPhoto -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(permissions)) {
                        requestGalleryPermission.launch(permissions)
                    } else {
                        openGallery()
                    }
                } else {
                    openGallery()
                }
            }

            R.id.btnAddProduct -> {
                if (isValid()) {
                    viewModel.addProduct(
                        AddProductRequest(
                            product_type = binding.edtProductType.text.toString().trim(),
                            product_name = binding.edtProductName.text.toString().trim(),
                            price = binding.edtProductPrice.text.toString().trim(),
                            tax = binding.edtProductTax.text.toString().trim(),
                            files = mProductImagePath
                        )
                    )
                }
            }
        }
    }

    private fun openGallery() {
        launcherGallery.launch(
            ImagePicker.with(requireActivity()).cropSquare().galleryOnly()
                .maxResultSize(1080, 1920, true).galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/png", "image/jpg", "image/jpeg"
                    )
                ).createIntent()
        )
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                uri.let { galleryUri ->
                    mProductImagePath = getPathFromInputStreamUri(mActivity!!, galleryUri)!!

                    Glide.with(mActivity!!).load(mProductImagePath).into(binding.imgProduct)
                }
            }
        }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    mActivity!!, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private val requestGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var allPermissionGranted = true
            for (permission in it.entries) {
                if (!permission.value) {
                    allPermissionGranted = false
                    break
                }
            }

            if (allPermissionGranted) {
                openGallery()
            } else {
                showToast("Please grant storage permission from App settings.")
            }
        }
}