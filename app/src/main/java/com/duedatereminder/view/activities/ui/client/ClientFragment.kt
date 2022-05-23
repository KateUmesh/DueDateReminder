package com.duedatereminder.view.activities.ui.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.databinding.FragmentClientBinding
import com.duedatereminder.view.activities.AddClientActivity
import com.duedatereminder.view.activities.CreateAccountActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientFragment : Fragment() {

    private lateinit var clientViewModel: ClientViewModel
    private var _binding: FragmentClientBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clientViewModel = ViewModelProvider(this).get(ClientViewModel::class.java)

        _binding = FragmentClientBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        val fab: FloatingActionButton = binding.fab
        clientViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        fab.setOnClickListener {
            val intent = Intent(context, AddClientActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}