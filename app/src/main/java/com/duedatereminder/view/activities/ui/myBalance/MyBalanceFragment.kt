package com.duedatereminder.view.activities.ui.myBalance

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.NotificationCategoriesAdapter
import com.duedatereminder.adapter.NotificationCategoriesFragmentAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentImportClientBinding
import com.duedatereminder.databinding.FragmentMyBalanceBinding
import com.duedatereminder.databinding.FragmentNotificationBinding
import com.duedatereminder.docpicker.DocPicker
import com.duedatereminder.docpicker.core.DocPickerConfig
import com.duedatereminder.model.DueDateCategories
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.view.activities.ClientDetailsToSendNotificationsActivity
import com.duedatereminder.view.activities.MyWb
import com.duedatereminder.viewModel.activityViewModel.ViewModelImportClientCsvFile
import com.duedatereminder.viewModel.activityViewModel.ViewModelMyBalance
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationCategories
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class MyBalanceFragment : Fragment(), SnackBarCallback {

    private var _binding: FragmentMyBalanceBinding? = null
    private val binding get() = _binding!!

    lateinit var mViewModelMyBalance: ViewModelMyBalance
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var tvNoData : TextView
    private lateinit var tvSmsBalance : TextView
    private lateinit var smsBalance : String


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyBalanceBinding.inflate(inflater, container, false)
        val root: View = binding.root



        /**Initialize Variables*/
        ll_loading = root.findViewById(R.id.ll_loading)
        tvNoData = root.findViewById(R.id.tvNoData)
        tvSmsBalance = root.findViewById(R.id.tvSmsBalance)

        /**Initialize View Model*/
        mViewModelMyBalance = ViewModelProvider(this)[ViewModelMyBalance::class.java]


        /**Call NotificationCategories GET Api*/
        callNotificationCategoriesApi()

        /**Response of NotificationCategories Api*/
        mViewModelMyBalance.mModelMyBalanceResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    tvNoData.visibility= View.GONE
                    if(!it.data!!.sms_balance.isNullOrEmpty()){
                        smsBalance = it.data?.sms_balance !!
                        tvSmsBalance.text = "Sms Balance :"+smsBalance
                    }else{
                        tvNoData.visibility= View.VISIBLE
                    }
                }
                "0"->{
                    tvNoData.visibility= View.VISIBLE
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
            mViewModelMyBalance.getMyBalance()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelMyBalance.getMyBalance()
    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }



}