package com.example.shoppingapp.Activities.TheMainActivity.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.Activities.AddProductActivity
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.Products
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentProductsBinding

class ProductsFragment :BaseFragment() {

    private var _binding: FragmentProductsBinding? = null
    private var products:ArrayList<Products>?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //notificationsViewModel =
            //ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onResume() {
        super.onResume()
        getProductDetails()
    }
    private fun getProductDetails(){
        showProgressDialog()
        FirestoreClass().getProductsSoldByUser(this)
    }
    fun productDetailsFetchSuccess(productsAdded:ArrayList<Products>){
        hideProgressDialog()
        products=productsAdded


        if(products!=null && products!!.size>0){
            val rv=view?.findViewById<RecyclerView>(R.id.pf_recyclerView)!!
            rv.visibility=View.VISIBLE
            view?.findViewById<TextView>(R.id.pf_noProductsMsg)?.visibility=View.GONE
            rv.layoutManager=LinearLayoutManager(activity)
            rv.setHasFixedSize(true)
            rv.adapter=ProductFragmentAdapter(requireActivity(),products!!,this)
        }
        else{
            view?.findViewById<TextView>(R.id.pf_noProductsMsg)?.visibility=View.VISIBLE
            val rv=view?.findViewById<RecyclerView>(R.id.pf_recyclerView)!!
            rv.visibility=View.GONE
        }
    }
    fun showAlertDialog(productId:String){
        val builder=AlertDialog.Builder(requireActivity())
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete this product?")

        builder.setPositiveButton("Yes"){dialogInterface,_->
            showProgressDialog()
            FirestoreClass().deleteProduct(this,productId)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface,_->
            dialogInterface.dismiss()
        }
        builder.show()
    }
    fun productDeleteSuccess(){
        hideProgressDialog()
        Toast.makeText(activity, "Product Deleted", Toast.LENGTH_SHORT).show()
        getProductDetails()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_products_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.Add_Product->{
                startActivity(Intent(activity, AddProductActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}