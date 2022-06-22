package com.duedatereminder.view.activities.ui.myBalance

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentMyBalanceBinding
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelMyBalance

class MyBalanceFragment : Fragment(), SnackBarCallback {

    private var _binding: FragmentMyBalanceBinding? = null
    private val binding get() = _binding!!

    lateinit var mViewModelMyBalance: ViewModelMyBalance
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var tvNoData : TextView
    private lateinit var tvSmsBalance : TextView
    private lateinit var smsBalance : String
    private lateinit var cvMyBalance : CardView


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
        cvMyBalance = root.findViewById(R.id.cvMyBalance)

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
                    cvMyBalance.visibility= View.VISIBLE
                    if(!it.data!!.sms_balance.isNullOrEmpty()){
                        smsBalance = it.data?.sms_balance !!
                        tvSmsBalance.text = "Sms Balance :"+smsBalance
                    }else{
                        tvNoData.visibility= View.VISIBLE
                        cvMyBalance.visibility= View.GONE
                    }
                }
                "0"->{
                    cvMyBalance.visibility= View.GONE
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