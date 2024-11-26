package com.example.admingrocerymart.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.admingrocerymart.R
import com.example.admingrocerymart.databinding.FragmentSigninBinding
import com.example.admingrocerymart.mesg



class SigninFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSigninBinding.inflate(layoutInflater)
        setstatusbarcolor()

        getusernumber()
        oncontinuebuttnclick()
        return binding.root
    }
    private fun oncontinuebuttnclick(){
        binding.btncontinue.setOnClickListener{
            val number=binding.userno.text.toString()
            if(number.isEmpty() || number.length!=10){
                mesg.showtoast(requireContext(), "Enter Valid Phone No")
            }
            else{
                val bundle=Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_signinFragment_to_OTPFragment,bundle)
            }
        }


    }
    private fun getusernumber(){
        binding.userno.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len = number?.length
                if(len == 10){
                    binding.btncontinue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkgreen))
                }
                else{
                    binding.btncontinue.setBackgroundColor(ContextCompat.getColor(requireContext(),
                        R.color.lightgray
                    ))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
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