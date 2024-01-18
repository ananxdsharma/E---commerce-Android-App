package com.example.ecommerceapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadProductActivity : AppCompatActivity() {
    private  lateinit var productPreviewImg:ImageView
    private  lateinit var edtProductName:EditText
    private  lateinit var edtProductPrice:EditText
    private  lateinit var edtProductDes:EditText
    private lateinit var btnSelectProduct:Button
    private lateinit var btnUploadProduct:Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_product)
        productPreviewImg=findViewById(R.id.product_img)
        edtProductName=findViewById(R.id.edt_product_name)
        edtProductPrice=findViewById(R.id.edt_product_price)
        edtProductDes=findViewById(R.id.product_des)
        btnSelectProduct=findViewById(R.id.btn_select_product)
        btnUploadProduct=findViewById(R.id.btn_upload_product)
        progressBar=findViewById(R.id.pb)
        btnSelectProduct.setOnClickListener {
            val galleryIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101 && resultCode== RESULT_OK){
            val uri =data!!.data
            productPreviewImg.setImageURI(uri)
            btnUploadProduct.setOnClickListener {
                progressBar.visibility= View.VISIBLE
                val productName=edtProductName.text.toString()
                val productPrice=edtProductPrice.text.toString()
                val productDes=edtProductDes.text.toString()
                val filename= UUID.randomUUID().toString()+".jpg"
                val storageRef= FirebaseStorage.getInstance().reference.child("productImages/$filename")
                storageRef.putFile(uri!!).addOnSuccessListener {
                    val result=it.metadata?.reference?.downloadUrl
                    result?.addOnSuccessListener {
                        uploadProduct(
                            productName,
                            productDes,
                            productPrice,
                            it.toString()
                        )
                    }
                }
        }

        }
    }

    private fun uploadProduct(name: String, des: String, price: String, link: String) {
              Firebase.database.getReference("products").child(name).setValue(
                  Product(
                      productName=name,
                      productDes=des,
                      productPrice = price,
                      productImage = link
                  )
              ).addOnSuccessListener {
                  progressBar.visibility=View.GONE
                  Toast.makeText(this, "Product Uploaded", Toast.LENGTH_SHORT).show()
              }
     }
}