package com.duedatereminder.view.fragments

import android.annotation.SuppressLint
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
import com.duedatereminder.adapter.AllNotificationTemplatesDisplayAdapter
import com.duedatereminder.adapter.ReminderReportAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentRemiderReportBinding
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.fragmentViewModel.ViewModelReminderReport

class ReminderReportFragment : Fragment(),SnackBarCallback {

    private var _binding: FragmentRemiderReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvReminderReport:RecyclerView
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var tvNoData: TextView
    private lateinit var mViewModelReminderReport: ViewModelReminderReport


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRemiderReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /**Initialize Variables*/
        rvReminderReport = root.findViewById(R.id.rvReminderReport)
        tvNoData = root.findViewById(R.id.tvNoData)
        ll_loading = root.findViewById(R.id.ll_loading)

        /**Initialize View Model*/
        mViewModelReminderReport = ViewModelProvider(this)[ViewModelReminderReport::class.java]


        /**Call DueDateReminder GET Api*/
        callDueDateReminderApi()

        /**Response of Contact Us GET Api*/
        mViewModelReminderReport.mModelReminderReportResponse.observe(viewLifecycleOwner, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    tvNoData.visibility = View.GONE
                    if(!it.data!!.report.isNullOrEmpty()){
                        val mAdapter = ReminderReportAdapter(this.requireContext(),it.data!!.report)
                        rvReminderReport.adapter=mAdapter
                    }
                }
                "0"->{
                    tvNoData.visibility = View.VISIBLE
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


    private fun callDueDateReminderApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelReminderReport.getDueDateReminderReport()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callDueDateReminderApi()
    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }
}