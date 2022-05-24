package com.duedatereminder.view.activities.ui.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.AllClientsAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentClientBinding
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.view.activities.AddClientActivity
import com.duedatereminder.viewModel.activityViewModel.ViewModelAllClient
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientFragment : Fragment(),SnackBarCallback {

    private lateinit var clientViewModel: ClientViewModel
    private var _binding: FragmentClientBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var mViewModelAllClient: ViewModelAllClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clientViewModel = ViewModelProvider(this).get(ClientViewModel::class.java)

        _binding = FragmentClientBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /**Initialize Variables*/
        val textView: TextView = binding.textGallery
        val fab: FloatingActionButton = binding.fab
        val rvAllClients: RecyclerView = binding.rvAllClients
        ll_loading = root.findViewById(R.id.ll_loading)

        /**Initialize View Model*/
        mViewModelAllClient = ViewModelProvider(this).get(ViewModelAllClient::class.java)

        clientViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        /**Fab Click*/
        fab.setOnClickListener {
            val intent = Intent(context, AddClientActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        /**Call All Clients GET Api*/
        callAllClientsApi()

        /**Response of All Clients GET Api*/
        mViewModelAllClient.mModelAllClientsResponse.observe(viewLifecycleOwner, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{

                    if(!it.data!!.clients.isNullOrEmpty()){
                        val mAdapter = AllClientsAdapter(this.requireContext(),it.data!!.clients!!)
                        rvAllClients.adapter=mAdapter
                    }
                }
                "0"->{
                    ContextExtension.snackBar(it.message,this.requireActivity())
                }
                else->{
                    activity?.showSnackBar(this,it.message)
                }
            }
        })

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callAllClientsApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelAllClient.allClients()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {

    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }
}