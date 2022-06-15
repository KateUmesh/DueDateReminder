package com.duedatereminder.view.activities.ui.home

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
import com.duedatereminder.adapter.BlogAdapter
import com.duedatereminder.adapter.NotificationCategoriesFragmentAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentHomeBinding
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationCategories
import com.duedatereminder.viewModel.fragmentViewModel.ViewModelHome


class HomeFragment : Fragment(), SnackBarCallback {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var rvHome: RecyclerView
    lateinit var mViewModelHome: ViewModelHome
    private lateinit var ll_loading : LinearLayoutCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        /**Initialize Variables*/
        rvHome = root.findViewById(R.id.rvHome)
        ll_loading = root.findViewById(R.id.ll_loading)
        ll_loading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelHome = ViewModelProvider(this)[ViewModelHome::class.java]

        /**Call Blogs GET Api*/
        callBlogsApi()

        /**Response of Blogs Api*/
        mViewModelHome.mModelBlogsResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{

                    if(!it.data!!.blogs.isNullOrEmpty()){
                        val mAdapter = BlogAdapter(this.requireContext(),it.data!!.blogs!!)
                        rvHome.adapter=mAdapter
                    }
                }
                "0"->{
                    ContextExtension.snackBar(it.message, this.requireActivity())
                }
                else->{
                    activity?.showSnackBar(this,it.message)
                }
            }
        })

        return root
    }

    private fun callBlogsApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelHome.getBlogs()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelHome.getBlogs()
    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}