package com.duedatereminder.view.activities.ui.notification

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
import com.duedatereminder.adapter.NotificationCategoriesAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentNotificationBinding
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationCategories

class NotificationFragment : Fragment(), SnackBarCallback {

    private lateinit var notificationViewModel: NotificationViewModel
    private var _binding: FragmentNotificationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var rvNotificationCategories: RecyclerView
    lateinit var mViewModelNotificationCategories: ViewModelNotificationCategories
    private lateinit var ll_loading : LinearLayoutCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationViewModel =
            ViewModelProvider(this).get(NotificationViewModel::class.java)

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        notificationViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        /**Initialize Variables*/
        rvNotificationCategories = root.findViewById(R.id.rvNotificationCategories)
        ll_loading = root.findViewById(R.id.ll_loading)

        ll_loading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelNotificationCategories = ViewModelProvider(this)[ViewModelNotificationCategories::class.java]


        /**Call NotificationCategories GET Api*/
        callNotificationCategoriesApi()

        /**Response of SendLoginOtp Api*/
        mViewModelNotificationCategories.mModelNotificationCategoriesResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{

                    if(!it.data!!.due_date_categories.isNullOrEmpty()){
                        val mAdapter = NotificationCategoriesAdapter(this.requireContext(),it.data!!.due_date_categories!!)
                        rvNotificationCategories.adapter=mAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callNotificationCategoriesApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelNotificationCategories.notificationCategories()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelNotificationCategories.notificationCategories()
    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }
}