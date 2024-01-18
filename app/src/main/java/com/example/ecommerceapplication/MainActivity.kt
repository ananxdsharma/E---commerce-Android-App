
package com.example.ecommerceapplication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var rv:RecyclerView
    private lateinit var productAdapter:ProductAdapter
    private lateinit var fab:FloatingActionButton
    val listofProduct= mutableListOf<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv=findViewById(R.id.rv)
        fab=findViewById(R.id.fab)
        FirebaseDatabase.getInstance().getReference("products")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listofProduct.clear()
                    for(dataSnapshot in snapshot.children){
                        val product=dataSnapshot.getValue(Product::class.java)
                        listofProduct.add(product!!)

                    }
                    productAdapter= ProductAdapter(listofProduct,this@MainActivity)
                    rv.adapter=productAdapter
                    rv.layoutManager=GridLayoutManager(this@MainActivity,2)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        fab.setOnClickListener{
            startActivity(
                Intent(this,LoginActivity::class.java)
            )
        }
    }
}