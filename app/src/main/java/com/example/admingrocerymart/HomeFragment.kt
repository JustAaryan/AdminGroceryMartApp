package com.example.admingrocerymart

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.admingrocerymart.adapter.AdapterProduct
import com.example.admingrocerymart.adapter.CategoriesAdapter
import com.example.admingrocerymart.databinding.EditProdlayoutBinding
import com.example.admingrocerymart.databinding.FragmentHomeBinding
import com.example.admingrocerymart.models.Categories
import com.example.admingrocerymart.models.Product
import com.example.admingrocerymart.viewmodels.Adminviewmodel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    val viewModel: Adminviewmodel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterProduct: AdapterProduct
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setstatusbarcolor()
        setcategories()
        searchproducts()
        getallrpoducts("All")
        return binding.root
    }

    private fun searchproducts() {
        binding.searchbar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                adapterProduct.filter?.filter(query)

            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun getallrpoducts(category: String) {
        binding.shimmercontainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchallprod(category).collect {
                if (it.isEmpty()) {
                    binding.rvproducts.visibility = View.GONE
                    binding.tvtext.visibility = View.VISIBLE
                } else {
                    binding.rvproducts.visibility = View.VISIBLE
                    binding.tvtext.visibility = View.GONE
                }
                adapterProduct = AdapterProduct(::oneditbtnclicked)
                binding.rvproducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originallist = it as ArrayList<Product>
                binding.shimmercontainer.visibility = View.GONE
            }
        }
    }

    private fun setcategories() {
        val catelougeList = ArrayList<Categories>()
        for (i in 0 until Constants.allprodcaticon.size) {
            catelougeList.add(Categories(Constants.allprodcat[i], Constants.allprodcaticon[i]))
        }
        binding.rvcategories.adapter = CategoriesAdapter(catelougeList, ::oncategoryclicked)
    }

    private fun setstatusbarcolor() {
        activity?.window?.apply {
            val statusbarcolor = ContextCompat.getColor(requireContext(), R.color.darkgreen)
            statusBarColor = statusbarcolor
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun oncategoryclicked(categories: Categories) {
        getallrpoducts(categories.category)
    }

    private fun oneditbtnclicked(product: Product) {


        val editproduct = EditProdlayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editproduct.apply {
            etprodtitle.setText(product.Prodtitle)
            etprodqty.setText(product.Prodqty.toString())
            etprodstock.setText(product.Prodstock.toString())
            etprodprice.setText(product.Prodprice.toString())
            etprodcat.setText(product.Prodcat)
            etprodtype.setText(product.Prodtype)
            etprodunit.setText(product.Produnit)
        }
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(editproduct.root)
            .create()
        alertDialog.show()
        editproduct.btneditprod.setOnClickListener {
            editproduct.etprodtitle.isEnabled = true
            editproduct.etprodqty.isEnabled = true
            editproduct.etprodunit.isEnabled = true
            editproduct.etprodstock.isEnabled = true
            editproduct.etprodprice.isEnabled = true
            editproduct.etprodcat.isEnabled = true
            editproduct.etprodtype.isEnabled = true
        }
        setautocompletetextview(editproduct)
        editproduct.btnSave.setOnClickListener{
            lifecycleScope.launch {
                product.Prodtitle = editproduct.etprodtitle.text.toString()
                product.Prodcat = editproduct.etprodcat.text.toString()
                product.Prodtype = editproduct.etprodtype.text.toString()
                product.Produnit = editproduct.etprodunit.text.toString()
                product.Prodprice = editproduct.etprodprice.text.toString().toInt()
                product.Prodqty = editproduct.etprodqty.text.toString().toInt()
                product.Prodstock = editproduct.etprodstock.text.toString().toInt()
                viewModel.editupdateprod(product)

            }

            mesg.showtoast(requireContext(),"Saved Changes!!!")
            alertDialog.dismiss()
        }
    }

    private fun setautocompletetextview(editproduct: EditProdlayoutBinding) {

        val units= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allprodunits)
        val category= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allprodcat)
        val prodtyp= ArrayAdapter(requireContext(),R.layout.show_list,Constants.allprodtypr)

        editproduct.apply {
            etprodtype.setAdapter(prodtyp)
            etprodcat.setAdapter(category)
            etprodunit.setAdapter(units)

        }

    }


}