package com.example.shoppingapp.Activities.TheMainActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.Activities.SettingActivity
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.Products
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentShopBinding

class ShopFragment : BaseFragment() {

    private var _binding: FragmentShopBinding? = null

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
        //homeViewModel =
         //   ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onResume() {
        super.onResume()
        getAllProductDetails()
    }
    private fun getAllProductDetails(){
        showProgressDialog()
        FirestoreClass().getAllProducts(this)
    }
    fun productDetailsFetchSuccess(products:ArrayList<Products>){
        hideProgressDialog()
        if(products.size>0){
            val rv=view?.findViewById<RecyclerView>(R.id.shopFragment_recyclerView)
            rv?.visibility=View.VISIBLE
            view?.findViewById<TextView>(R.id.shopFragment_noItemsMessage)?.visibility=View.GONE


            rv?.layoutManager= GridLayoutManager(activity,2)
            rv?.adapter=ShopFragmentRvAdapter(requireActivity(),products)
        }
        else{
            val rv=view?.findViewById<RecyclerView>(R.id.shopFragment_recyclerView)!!
            rv.visibility=View.GONE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shopmenu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings->{
                startActivity(Intent(activity,SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}