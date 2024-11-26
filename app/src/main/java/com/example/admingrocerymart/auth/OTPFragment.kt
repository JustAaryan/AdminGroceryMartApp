package com.example.admingrocerymart.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.admingrocerymart.AdminMainActivity
import com.example.admingrocerymart.R
import com.example.admingrocerymart.databinding.FragmentOTPBinding
import com.example.admingrocerymart.mesg
import com.example.admingrocerymart.models.Admins
import com.example.admingrocerymart.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentOTPBinding
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOTPBinding.inflate(layoutInflater)
        getusrno()
        edituserno()
        sendotp()
        loginbtnclicked()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backbtn.setOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signinFragment)
        }
    }


    private fun edituserno(){
        val edittext= arrayOf(binding.optno1,binding.optno2,binding.optno3,binding.optno4,binding.optno5,binding.optno6)
        for(i in edittext.indices)
        {
            edittext[i].addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if(s?.length==1)
                    {
                        if(i<edittext.size-1){
                            edittext[i+1].requestFocus()
                        }
                    }
                    else if(s?.length==0){
                        if(i>0){
                            edittext[i-1].requestFocus()
                        }
                    }
                }

            })
        }
    }
    private fun getusrno(){
        val bundle=arguments
        userNumber=bundle?.getString("number").toString()
        binding.usrphno.text=userNumber
    }
    private fun sendotp(){
        mesg.showdialog(requireContext(), message = "Sending OTP...")
        viewModel.apply { sendotp(userNumber,requireActivity())
            lifecycleScope.launch{
                otpsent.collect{
                    if(it){
                        mesg.hidedialog()
                        mesg.showtoast(requireContext(), "OTP Sent...")
                        kotlinx.coroutines.delay(2000)}

                }
            }
        }
    }



    private fun loginbtnclicked(){

        binding.otploginbtn.setOnClickListener{
            mesg.showdialog(requireContext(), "All Set...")
            val edittext= arrayOf(binding.optno1,binding.optno2,binding.optno3,binding.optno4,binding.optno5,binding.optno6)
            val otp=edittext.joinToString (""){it.text.toString() }
            if(otp.length<edittext.size){
                mesg.showtoast(requireContext(), "Invalid OTP")
            }
            else{
                edittext.forEach { it.text?.clear();it.clearFocus() }
                verifyotp(otp)

            }
        }
    }

    private fun verifyotp(otp: String) {
        val admins= Admins(uid=null, adminPhoneNumber = userNumber)
        viewModel.signInWithPhoneAuthCredential(otp,userNumber,admins)
        lifecycleScope.launch {
            viewModel.signindone.collect{
                if(it){
                    mesg.hidedialog()
                    mesg.showtoast(requireContext(), "Welcome")
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

    }


}

