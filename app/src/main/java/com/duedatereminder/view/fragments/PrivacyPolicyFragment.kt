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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.duedatereminder.R
import com.duedatereminder.databinding.FragmentAboutUsBinding
import com.duedatereminder.databinding.FragmentImportClientBinding
import com.duedatereminder.databinding.FragmentPrivacyPolicyBinding
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.LocalSharedPreference

class PrivacyPolicyFragment : Fragment() {

    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!
    var wVPrivacyPolicy: WebView? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        val root: View = binding.root



        wVPrivacyPolicy = root.findViewById(R.id.wVPrivacyPolicy)
        wVPrivacyPolicy!!.getSettings().javaScriptEnabled = true
        wVPrivacyPolicy!!.getSettings().javaScriptCanOpenWindowsAutomatically = true
        wVPrivacyPolicy!!.loadUrl(Constant.baseUrl+Constant.privacy_policy)




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}