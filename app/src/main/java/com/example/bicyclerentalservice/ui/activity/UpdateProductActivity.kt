package com.example.bicyclerentalservice.ui.activity



import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bicyclerentalservice.R
import com.example.bicyclerentalservice.databinding.ActivityUpdateProductBinding
import com.example.bicyclerentalservice.repository.ProductRepistoryImpl
import com.example.bicyclerentalservice.repository.ProductViewModel

import com.example.bicyclerentalservice.utils.LoadingUtils


class UpdateProductActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateProductBinding
    lateinit var productViewModel: ProductViewModel

    lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = ProductRepistoryImpl()
        productViewModel = ProductViewModel(repo)

        var productId: String? = intent.getStringExtra("productId")

        loadingUtils = LoadingUtils(this)

        productViewModel.getProductById(productId.toString())

        productViewModel.products.observe(this) {
            binding.updateProductDesc.setText(it?.productDesc.toString())
            binding.updateProductprice.setText(it?.price.toString())
            binding.updateProductName.setText(it?.productName.toString())
        }

        binding.btnUpdateProduct.setOnClickListener {
            loadingUtils.show()
            val productName = binding.updateProductName.text.toString()
            val price = binding.updateProductprice.text.toString().toInt()
            val productDesc = binding.updateProductDesc.text.toString()

            var updatedMap = mutableMapOf<String, Any>()

            updatedMap["productName"] = productName
            updatedMap["productDesc"] = productDesc
            updatedMap["price"] = price

            productViewModel.updateProduct(productId.toString() , updatedMap) {
                    success , message ->
                if (success){
                    loadingUtils.dismiss()
                    Toast.makeText(this@UpdateProductActivity, message.toString(), Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    loadingUtils.dismiss()
                    Toast.makeText(this@UpdateProductActivity, message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}