package com.example.shoppingapp.Activities.TheMainActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.Activities.CartActivity
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.Orders
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentOrdersBinding

class OrdersFragment : BaseFragment() {

    private lateinit var ordered:ArrayList<Orders>
    private var _binding: FragmentOrdersBinding? = null

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
        //dashboardViewModel =
            //ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onResume() {
        super.onResume()
        getOrderedProducts()
    }
    private fun getOrderedProducts(){
       showProgressDialog()
       FirestoreClass().getOrderedProducts(this)
    }
    fun orderedProductFetchSuccess(orders:ArrayList<Orders>){
        hideProgressDialog()
        ordered=orders

        if(ordered.size>0){
            val rv=view?.findViewById<RecyclerView>(R.id.orderFrag_rv)
            rv!!.visibility=View.VISIBLE
            view?.findViewById<TextView>(R.id.orderFrag_noOrderMsg)!!.visibility=View.GONE

            rv.setHasFixedSize(true)

            rv.adapter=OrderFragmentRvAdapter(requireActivity(),ordered)
            rv.layoutManager=LinearLayoutManager(context)
        }
        else{
            view?.findViewById<RecyclerView>(R.id.orderFrag_rv)!!.visibility=View.GONE
            view?.findViewById<TextView>(R.id.orderFrag_noOrderMsg)!!.visibility=View.VISIBLE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cart_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cart_menu->{
                startActivity(Intent(requireActivity(),CartActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}