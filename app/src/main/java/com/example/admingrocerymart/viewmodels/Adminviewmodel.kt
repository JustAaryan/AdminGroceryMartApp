package com.example.admingrocerymart.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.admingrocerymart.mesg
import com.example.admingrocerymart.models.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class Adminviewmodel: ViewModel() {

    private val _isimageuploaded= MutableStateFlow(false)
    var isimageuploaded:StateFlow<Boolean> = _isimageuploaded
private val _downloadedurl = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    var downloadurl:StateFlow<ArrayList<String?>> = _downloadedurl
    private val _isprodsaved =MutableStateFlow(false)
    var isprodsaved:StateFlow<Boolean> =_isprodsaved
    fun saveimagesindb(imageUri: ArrayList<Uri>){
        val downloadurls= ArrayList<String?>()

        imageUri.forEach{uri ->
            val imageref= FirebaseStorage.getInstance().reference.child(mesg.getcurrentuserid()).child("Images").child(UUID.randomUUID().toString())
            imageref.putFile(uri).continueWithTask{
                imageref.downloadUrl
            }.addOnCompleteListener{task->
                val url = task.result
                downloadurls.add(url.toString())
                if(downloadurls.size== imageUri.size){
                    _isimageuploaded.value = true
                    _downloadedurl.value= downloadurls
                }

            }
        }

    }
    fun saveproduct(product: Product){
        FirebaseDatabase.getInstance().getReference("Admin").child("All Products/${product.prod_id}").setValue(product)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Admin").child("ProductCategory/${product.Prodcat}/${product.prod_id}").setValue(product)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("Admin").child("ProductType/${product.Prodtype}/${product.prod_id}").setValue(product)
                            .addOnSuccessListener {
                                _isprodsaved.value = true
                            }
                    }
            }



    }
    fun fetchallprod(category: String): Flow<List<Product>> = callbackFlow {
        val db =  FirebaseDatabase.getInstance().getReference("Admin").child("All Products")
        val eventlistener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for(product in snapshot.children){
                    val prod = product.getValue(Product::class.java)
                    if (category == "All" || prod?.Prodcat == category ) {
                        products.add(prod!!)
                    }

                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        db.addValueEventListener(eventlistener)
        awaitClose{db.removeEventListener(eventlistener)}
    }

fun editupdateprod(product: Product){
    FirebaseDatabase.getInstance().getReference("Admin").child("All Products/${product.prod_id}").setValue(product)
    FirebaseDatabase.getInstance().getReference("Admin").child("ProductCategory/${product.Prodcat}/${product.prod_id}").setValue(product)
    FirebaseDatabase.getInstance().getReference("Admin").child("ProductType/${product.Prodtype}/${product.prod_id}").setValue(product)
}


}