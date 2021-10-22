package com.example.shoppingapp.Activities.TheMainActivity.Fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoppingapp.R


open class BaseFragment : Fragment() {
    private lateinit var progressDialog: Dialog

    fun showProgressDialog(){
        progressDialog=Dialog(requireActivity())
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setTitle("Progress Dialog")

        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }
    fun hideProgressDialog(){
        progressDialog.dismiss()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

}