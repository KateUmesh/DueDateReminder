package com.duedatereminder.view.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelAddClientRequest
import com.duedatereminder.model.ModelDeleteClientRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toast
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelEditClient
import com.duedatereminder.viewModel.activityViewModel.ViewModelViewProfile

class ViewClientProfileActivity : AppCompatActivity(), SnackBarCallback {
    private lateinit var tvClientName:TextView
    private lateinit var tvFirstLetter:TextView
    private lateinit var tvMobile:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvWhatsapp:TextView
    private lateinit var tvCity:TextView
    private lateinit var tvNotificationCategories:TextView
    private lateinit var nsClientProfile:NestedScrollView
    private lateinit var ibChat:ImageButton
    private lateinit var ibCall:ImageButton
    private lateinit var mViewModelViewProfile: ViewModelViewProfile
    private lateinit var ll_loading : LinearLayoutCompat
    var idClient:String = ""
    var nameClient:String = ""
    var mobile:String = ""
    var whatsappNum:String = ""
    var mState:String = ""
    var flag=0;
    private lateinit var dueDateCategoriesNames:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_client_profile)

        /**Get Client id From AllClientAdapter*/
        if(!intent.getStringExtra(Constant.ID_CLIENT).isNullOrEmpty()){
            idClient =  intent.getStringExtra(Constant.ID_CLIENT)!!
            nameClient =  intent.getStringExtra(Constant.NAME)!!
            mState =  intent.getStringExtra(Constant.MENU_STATE)!!
        }

        /**Toolbar*/
        toolbar(nameClient,true)

        /**Initialize Variables*/
        tvClientName = findViewById(R.id.tvClientName)
        tvFirstLetter = findViewById(R.id.tvFirstLetter)
        tvMobile = findViewById(R.id.tvMobile)
        tvEmail = findViewById(R.id.tvEmail)
        tvWhatsapp = findViewById(R.id.tvWhatsapp)
        tvCity = findViewById(R.id.tvCity)
        tvNotificationCategories = findViewById(R.id.tvNotificationCategories)
        nsClientProfile = findViewById(R.id.nsClientProfile)
        ibChat = findViewById(R.id.ibChat)
        ibCall = findViewById(R.id.ibCall)
        ll_loading = findViewById(R.id.ll_loading)
        dueDateCategoriesNames = ArrayList()
        invalidateOptionsMenu()

        /**Initialize View Model*/
        mViewModelViewProfile = ViewModelProvider(this).get(ViewModelViewProfile::class.java)


        /**Response of EditClient GET Api*/
        mViewModelViewProfile.mModelEditClientGetResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE

            when(it.status){
                "1"->{
                    nsClientProfile.visibility = View.VISIBLE
                    /*Set name*/
                    if(it.data?.client!!.name.isNotEmpty()){
                        tvClientName.text = it.data?.client!!.name
                    }

                    /*Set First Character*/
                    if(it.data?.client!!.name.isNotEmpty()) {
                        val charArray = it.data?.client!!.name.toCharArray()
                        tvFirstLetter.text = charArray[0].toString()
                    }

                    /*Set mobile*/
                    if(it.data?.client!!.mobile.isNotEmpty()){
                        tvMobile.text = it.data?.client!!.mobile
                        mobile = it.data?.client!!.mobile
                        /**Call Button Click*/
                        ibCall.setOnClickListener {
                            ActivityCompat.requestPermissions((this as Activity), arrayOf(Manifest.permission.CALL_PHONE), 1)
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            ) {
                                //Creating intents for making a call
                                val callIntent = Intent(Intent.ACTION_DIAL)
                                callIntent.data = Uri.parse("tel:$mobile")
                                startActivity(callIntent)
                            } else {
                                toast("You don't assign permission")
                            }
                        }
                    }

                    /*Set whatsapp*/
                    if(!it.data?.client!!.whatsapp.isNullOrEmpty()){
                        tvWhatsapp.text = it.data?.client!!.whatsapp
                        whatsappNum = it.data?.client!!.whatsapp
                        /**Chat Button Click*/
                        ibChat.setOnClickListener {
                            val url  = "https://api.whatsapp.com/send?phone=+91$whatsappNum"
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            startActivity(intent)
                        }
                    }

                    /*Set email*/
                    if(it.data?.client!!.email.isNotEmpty()){
                        tvEmail.text = it.data?.client!!.email
                    }

                    /*Set address*/
                    if(it.data?.client!!.address.isNotEmpty()){
                        tvCity.text = it.data?.client!!.address
                    }

                    /*Set dueDateCategories*/
                    if(!it.data?.client!!.due_date_categories.isNullOrEmpty()){
                        val selectedCategoriesList = it.data?.client!!.due_date_categories
                        for(i in 0 until selectedCategoriesList.size){
                            for(j in 0 until it.data?.due_date_categories!!.size){
                                if(selectedCategoriesList[i].equals(it.data?.due_date_categories!![j].id_due_date_category)){
                                    dueDateCategoriesNames.add(it.data?.due_date_categories!![j].category_name)
                                }
                            }
                        }
                        val categories: String = TextUtils.join(", ", dueDateCategoriesNames)
                        tvNotificationCategories.text = categories

                    }
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })


        /**Response of DeleteClient POST Api*/
        mViewModelViewProfile.mModelDeleteClientResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    showOkDialog(it.message,this)
                }
                "0"->{

                    snackBar(it.message,this)
                }
                else ->{
                    showSnackBar(this,it.message)
                }
            }
        })
    }

    private fun callEditClientGetApi(){
        flag=0
        nsClientProfile.visibility = View.GONE
        ll_loading.visibility = View.VISIBLE
        dueDateCategoriesNames.clear()
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelViewProfile.editClient(idClient.toInt())
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callDeleteClientGetApi(){
        flag=1
        ll_loading.visibility = View.VISIBLE
        val modelDeleteClientRequest= ModelDeleteClientRequest(idClient)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelViewProfile.deleteClient(modelDeleteClientRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }


    override fun snackBarSuccessInternetConnection() {
        if(flag==0) {
            callEditClientGetApi()
        }else{
            callDeleteClientGetApi()
        }
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_menu, menu)
        if (mState == "HIDE_MENU") {
            for (i in 0 until menu.size()) menu.getItem(i).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, EditClientActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(Constant.ID_CLIENT,idClient)
                startActivity(intent)
                true
            }
            R.id.action_delete -> {

               showDeleteClientDialog(getString(R.string.delete_client_message),this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        /**Call EditClient GET Api*/
        callEditClientGetApi()
    }

    fun showDeleteClientDialog(message: String,context: Context) {
        val builder =
            AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.app_name) as CharSequence)
        builder.setMessage(message)
        builder.setPositiveButton(
            R.string.delete
        ) { _, _ ->
            callDeleteClientGetApi()
        }
        builder.setNegativeButton(
            R.string.cancel
        ) { _, _ ->

        }
        builder.show()
    }

}