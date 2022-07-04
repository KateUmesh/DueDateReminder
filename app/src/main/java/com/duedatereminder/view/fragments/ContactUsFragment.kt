package com.duedatereminder.view.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.adapter.AllClientsAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentAboutAppBinding
import com.duedatereminder.databinding.FragmentAboutUsBinding
import com.duedatereminder.databinding.FragmentContactUsBinding
import com.duedatereminder.databinding.FragmentImportClientBinding
import com.duedatereminder.model.ModelSubmitFeedbackRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showOkFinishActivityDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.fragmentViewModel.ViewModelAllClient
import com.duedatereminder.viewModel.fragmentViewModel.ViewModelContactUs
import com.google.android.material.textfield.TextInputEditText

class ContactUsFragment : Fragment(),SnackBarCallback {

    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    private lateinit var llContactUs: LinearLayout
    private lateinit var tvMobile: TextView
    private lateinit var tvBusinessEmail: TextView
    private lateinit var tvAppEmail: TextView
    private lateinit var edtSendFeedback: TextInputEditText
    private lateinit var btnSendFeedback: Button
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var tvNoData: TextView
    private lateinit var mViewModelContactUs: ViewModelContactUs


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        /**Initialize Variables*/
        llContactUs = root.findViewById(R.id.llContactUs)
        tvMobile = root.findViewById(R.id.tvMobile)
        tvBusinessEmail = root.findViewById(R.id.tvBusinessEmail)
        tvAppEmail = root.findViewById(R.id.tvAppEmail)
        tvNoData = root.findViewById(R.id.tvNoData)
        edtSendFeedback = root.findViewById(R.id.edtSendFeedback)
        btnSendFeedback = root.findViewById(R.id.btnSendFeedback)
        ll_loading = root.findViewById(R.id.ll_loading)

        /**Initialize View Model*/
        mViewModelContactUs = ViewModelProvider(this)[ViewModelContactUs::class.java]


        /**Call Contact Us GET Api*/
        callContactUsApi()

        /**Response of Contact Us GET Api*/
        mViewModelContactUs.mModelContactUsResponse.observe(viewLifecycleOwner, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    tvNoData.visibility = View.GONE
                    llContactUs.visibility = View.VISIBLE

                    /*Set Mobile Number*/
                    if(it.data!!.customer_care_mobile.isNotEmpty()){
                        tvMobile.text = it.data!!.customer_care_mobile
                    }

                    /*Set Business Email*/
                    if(it.data!!.business_queries_email.isNotEmpty()){
                        tvBusinessEmail.text = it.data!!.business_queries_email
                    }

                    /*Set App Email*/
                    if(it.data!!.app_related_queries_email.isNotEmpty()){
                        tvAppEmail.text = it.data!!.app_related_queries_email
                    }



                }
                "0"->{
                    tvNoData.visibility = View.VISIBLE
                    snackBar(it.message,this.requireActivity())
                }
                else->{
                    activity?.showSnackBar(this,it.message)
                }
            }
        })

        /** Button Send Feedback Click*/
        btnSendFeedback.setOnClickListener {
            if(edtSendFeedback.text.toString().trim().isNotEmpty()){
                callSubmitFeedbackApi(edtSendFeedback.text.toString())
            }else{
                snackBar(getString(R.string.enter_feedback),this.requireActivity())
            }
        }

        /**Response of SubmitFeedback POST Api*/
        mViewModelContactUs.mModelSubmitFeedbackResponse.observe(viewLifecycleOwner, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    showOkFinishActivityDialog(it.message,requireActivity())
                }
                "0"->{
                    tvNoData.visibility = View.VISIBLE
                    snackBar(it.message,this.requireActivity())
                }
                else->{
                    activity?.showSnackBar(this,it.message)
                }
            }
        })


        return root
    }

    private fun callContactUsApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelContactUs.getContactUs()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callSubmitFeedbackApi(feedback:String){
        ll_loading.visibility = View.VISIBLE
        var mModelSubmitFeedbackRequest = ModelSubmitFeedbackRequest(feedback)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelContactUs.submitFeedback(mModelSubmitFeedbackRequest)
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun snackBarSuccessInternetConnection() {
        callContactUsApi()
    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }


}