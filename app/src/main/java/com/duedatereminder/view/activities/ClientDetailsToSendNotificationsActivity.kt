package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationTemplates

class ClientDetailsToSendNotificationsActivity : AppCompatActivity() {
    lateinit var rvClientDetails: RecyclerView
    private lateinit var mViewModelNotificationTemplates: ViewModelNotificationTemplates
    private lateinit var llLoading : LinearLayoutCompat
    private var idNotificationCategory:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_details_to_send_notifications)
    }
}