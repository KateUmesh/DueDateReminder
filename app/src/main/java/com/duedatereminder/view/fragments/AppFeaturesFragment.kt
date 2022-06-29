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
import com.duedatereminder.databinding.FragmentAppFeaturesBinding
import com.duedatereminder.databinding.FragmentImportClientBinding
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.LocalSharedPreference

class AppFeaturesFragment : Fragment() {

    private var _binding: FragmentAppFeaturesBinding? = null
    private val binding get() = _binding!!
    var wVAppFeatures: WebView? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAppFeaturesBinding.inflate(inflater, container, false)
        val root: View = binding.root



        wVAppFeatures = root.findViewById(R.id.wVAppFeatures)
        wVAppFeatures!!.getSettings().javaScriptEnabled = true
        wVAppFeatures!!.getSettings().javaScriptCanOpenWindowsAutomatically = true
        wVAppFeatures!!.loadUrl(Constant.baseUrl+Constant.app_features)




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}