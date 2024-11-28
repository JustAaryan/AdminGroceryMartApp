package com.example.admingrocerymart

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.admingrocerymart.adapter.Adapterselectedimg
import com.example.admingrocerymart.databinding.FragmentAddproductBinding
import com.example.admingrocerymart.models.Product
import com.example.admingrocerymart.viewmodels.Adminviewmodel
import kotlinx.coroutines.launch


class addproductFragment : Fragment() {
private val viewModel: Adminviewmodel by viewModels ()
private lateinit var binding: FragmentAddproductBinding
private val imageUris:ArrayList<Uri> = arrayListOf()
    val selectedimage=   registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
            listOfUri->
        val maximg=listOfUri.take(5)
        imageUris.clear()
        imageUris.addAll(maximg)
        binding.rvprodimg.adapter= Adapterselectedimg(imageUris)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding=FragmentAddproductBinding.inflate(layoutInflater)

        setstatusbarcolor()
        setautocompletetextview()
        onImageSelectClicked()
        onaddbuttonClicked()
      return binding.root

    }

    private fun onaddbuttonClicked() {
        binding.btnaddprod.setOnClickListener{
            mesg.showdialog(requireContext(),"Uploading...")
            val Prodtitle=binding.prodtitle.text.toString()
            val Prodcat=binding.prodcat.text.toString()
            val Prodtype=binding.prodtype.text.toString()
            val Prodqty=binding.prodqty.text.toString()
            val Produnit=binding.produnit.text.toString()
            val Prodprice=binding.addprodprice.text.toString()
            val Prodstock=binding.prodstock.text.toString()

            if (Prodstock.isEmpty()||Prodtitle.isEmpty()||Prodcat.isEmpty()||Prodtype.isEmpty()||Prodqty.isEmpty()||Produnit.isEmpty()||Prodprice.isEmpty()){
                mesg.apply{hidedialog()
                showtoast(requireContext(),"Empty Field Not Allowed")}
            }
            else if(imageUris.isEmpty()){
                mesg.apply{hidedialog()
                    showtoast(requireContext(),"Please Select Some Images")}
            }
            else{
                val product =Product(Prodtitle=Prodtitle,
                    Prodcat=Prodcat,
                    Prodtype = Prodtype,
                    Prodqty = Prodqty.toInt(),
                    Produnit=Produnit,
                    Prodprice=Prodprice.toInt(),
                    Prodstock=Prodstock.toInt(),
                    itemcount = 0,
                    admin_uid = mesg.getcurrentuserid(),
                    prod_id = mesg.returnRandomID()

                )
                saveimage(product)

            }
        }
    }

    private fun saveimage(product: Product)
    {
viewModel.saveimagesindb(imageUris)
        lifecycleScope.launch {
            viewModel.isimageuploaded.collect{
                if(it){

                    mesg.apply{hidedialog()
                        showtoast(requireContext(),"Images Uploaded")}
                    geturls(product)
                }
            }
        }
        
    }

    private fun geturls(product: Product) {
mesg.showdialog(requireContext(),"Publishing Product...")
        lifecycleScope.launch {
            viewModel.downloadurl.collect{
                val urls = it
                product.Prodimageuris= urls
                saveproduct(product)
            }
        }
    }

    private fun saveproduct(product: Product) {

viewModel.saveproduct(product)
        lifecycleScope.launch {
            viewModel.isprodsaved.collect {
                if (it) {
mesg.hidedialog()
                    startActivity(Intent(requireActivity(),AdminMainActivity::class.java))
                    mesg.showtoast(requireContext(),"Your Product is Updated")

                }
            }
        }

    }

    private fun onImageSelectClicked() {
        binding.selectimg.setOnClickListener{
        selectedimage .launch("image/*")
        }
    }


    private fun setautocompletetextview() {

        val units= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allprodunits)
        val category= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allprodcat)
        val prodtyp= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allprodtypr)

        binding.apply {
            prodtype.setAdapter(prodtyp)
            prodcat.setAdapter(category)
            produnit.setAdapter(units)

        }

    }

    private fun setstatusbarcolor(){
        activity?.window?.apply{
            val statusbarcolor= ContextCompat.getColor(requireContext(), R.color.darkgreen)
            statusBarColor=statusbarcolor
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}