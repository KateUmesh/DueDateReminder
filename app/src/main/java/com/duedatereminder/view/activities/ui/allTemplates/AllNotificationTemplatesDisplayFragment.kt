package com.duedatereminder.view.activities.ui.allTemplates

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.AllNotificationTemplatesDisplayAdapter
import com.duedatereminder.adapter.NotificationCategoriesAdapter
import com.duedatereminder.adapter.NotificationCategoriesFragmentAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentAllNotificationTemplatesDisplayBinding
import com.duedatereminder.databinding.FragmentNotificationBinding
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelAllNotificationTemplatesDisplay
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationCategories

class AllNotificationTemplatesDisplayFragment : Fragment(), SnackBarCallback,AllNotificationTemplatesDisplayAdapter.OnChatClickListener {


    private var _binding: FragmentAllNotificationTemplatesDisplayBinding? = null
    private val binding get() = _binding!!

    lateinit var rvAllNotificationTemplatesDisplay: RecyclerView
    private lateinit var mViewModelAllNotificationTemplatesDisplay: ViewModelAllNotificationTemplatesDisplay
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var tvNoData : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentAllNotificationTemplatesDisplayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /**Initialize Variables*/
        rvAllNotificationTemplatesDisplay = root.findViewById(R.id.rvAllNotificationTemplatesDisplay)
        ll_loading = root.findViewById(R.id.ll_loading)
        tvNoData = root.findViewById(R.id.tvNoData)
        ll_loading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelAllNotificationTemplatesDisplay = ViewModelProvider(this)[ViewModelAllNotificationTemplatesDisplay::class.java]


        /**Call AllNotificationTemplatesDisplay GET Api*/
        callAllNotificationTemplatesDisplayApi()

        /**Response of AllNotificationTemplatesDisplay Api*/
        mViewModelAllNotificationTemplatesDisplay.mModelAllNotificationTemplatesDisplayResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    if(!it.data!!.templates.isNullOrEmpty()){
                        tvNoData.visibility = View.GONE
                        val mAdapter = AllNotificationTemplatesDisplayAdapter(this.requireContext(),it.data!!.templates!!,0,this)
                        rvAllNotificationTemplatesDisplay.adapter=mAdapter
                    }else{
                        tvNoData.visibility = View.VISIBLE
                    }
                }
                "0"->{
                    tvNoData.visibility = View.VISIBLE
                    snackBar(it.message, this.requireActivity())
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

    private fun callAllNotificationTemplatesDisplayApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelAllNotificationTemplatesDisplay.getAllNotificationTemplatesDisplay()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelAllNotificationTemplatesDisplay.getAllNotificationTemplatesDisplay()
    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }

    override fun onChatItemClick(template: String) {
        activity!!.copyToClipboard(template)
    }

    fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
        snackBar("Template copied", context as Activity)
    }
}