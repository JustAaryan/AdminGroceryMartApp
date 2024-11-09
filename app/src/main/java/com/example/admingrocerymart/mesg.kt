package com.example.admingrocerymart

import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.admingrocerymart.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth

object mesg {
    private var dialog : AlertDialog? = null
    fun showdialog(context : android.content.Context,message: String){

        val progress= ProgressDialogBinding.inflate(LayoutInflater.from(context))
        progress.Message.text=message
        dialog= AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog?.show()
    }
    fun hidedialog(){
        dialog?.dismiss()
    }
    fun showtoast(context: android.content.Context, message: String){
        Toast.makeText(context , message, Toast.LENGTH_SHORT).show()
    }
    private var firebaseAuthInstance: FirebaseAuth?=null
    fun getAuthInstance(): FirebaseAuth {
        if(firebaseAuthInstance==null){
            firebaseAuthInstance= FirebaseAuth.getInstance()
        }
        return firebaseAuthInstance!!
    }

    fun getcurrentuserid() : String{
        return FirebaseAuth.getInstance().currentUser?.uid ?:""
    }
}