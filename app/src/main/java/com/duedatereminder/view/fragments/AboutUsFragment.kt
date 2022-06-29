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
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.LocalSharedPreference

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!
    var wVAboutUs: WebView? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        wVAboutUs = root.findViewById(R.id.wVAboutUs)
        wVAboutUs!!.getSettings().javaScriptEnabled = true
        wVAboutUs!!.getSettings().javaScriptCanOpenWindowsAutomatically = true
        wVAboutUs!!.loadUrl(Constant.baseUrl+Constant.about_us)




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}