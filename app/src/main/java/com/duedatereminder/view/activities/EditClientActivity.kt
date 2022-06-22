package com.duedatereminder.view.activities

import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.isDigitsOnly
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.*
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showOkFinishActivityDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelEditClient
import com.google.android.material.textfield.TextInputEditText


class EditClientActivity : AppCompatActivity(), SnackBarCallback {
    private lateinit var tietName : TextInputEditText
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietMobileNumber : TextInputEditText
    private lateinit var tietWhatsappNumber : TextInputEditText
    private lateinit var tietAddress : TextInputEditText
    private lateinit var edtNotificationCategories : TextInputEditText
    private lateinit var ll_loading : LinearLayoutCompat
    var otp:String = ""
    var idClient:String = ""
    private lateinit var mViewModelEditClient: ViewModelEditClient
    //private lateinit var dueDateCategories:ArrayList<String>
    private lateinit var dueDateCategoriesNames:ArrayList<String>
    private var dueDateCategoriesList = ArrayList<DueDateCategories>()
    private var selectedCategoriesList = ArrayList<String>()
    private var checkedItems=ArrayList<Boolean>()
    private var LAUNCH_SECOND_ACTIVITY:Int=1
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_client)

        /**Toolbar*/
        toolbar(getString(R.string.edit_client),true)

        /**Initialize variables*/
        tietName  = findViewById(R.id.tietName)
        tietEmail = findViewById(R.id.tietEmail)
        tietMobileNumber = findViewById(R.id.tietMobileNumber)
        tietWhatsappNumber = findViewById(R.id.tietWhatsappNumber)
        tietAddress = findViewById(R.id.tietAddress)
        edtNotificationCategories = findViewById(R.id.edtNotificationCategories)
        ll_loading = findViewById(R.id.ll_loading)
        val btnSubmit : Button = findViewById(R.id.btnSubmit)
        val nsEditClient : NestedScrollView = findViewById(R.id.nsEditClient)
        //dueDateCategories = ArrayList()
        dueDateCategoriesNames = ArrayList()

        /**Get Client id From AllClientAdapter*/
        if(!intent.getStringExtra(Constant.ID_CLIENT).isNullOrEmpty()){
           idClient =  intent.getStringExtra(Constant.ID_CLIENT)!!
        }

        /**Initialize View Model*/
        mViewModelEditClient = ViewModelProvider(this).get(ViewModelEditClient::class.java)


        /**Call EditClient GET Api*/
        callEditClientGetApi()


        /**Response of EditClient GET Api*/
        mViewModelEditClient.mModelEditClientGetResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE

            when(it.status){
                "1"->{
                    nsEditClient.visibility = View.VISIBLE
                    /*Set name*/
                    if(it.data?.client!!.name.isNotEmpty()){
                       tietName.setText(it.data?.client!!.name)
                    }

                    /*Set mobile*/
                    if(it.data?.client!!.mobile.isNotEmpty()){
                        tietMobileNumber.setText(it.data?.client!!.mobile)
                    }

                    /*Set whatsapp*/
                    if(!it.data?.client!!.whatsapp.isNullOrEmpty()){
                        tietWhatsappNumber.setText(it.data?.client!!.whatsapp)
                    }

                    /*Set email*/
                    if(it.data?.client!!.email.isNotEmpty()){
                        tietEmail.setText(it.data?.client!!.email)
                    }

                    /*Set address*/
                    if(it.data?.client!!.address.isNotEmpty()){
                        tietAddress.setText(it.data?.client!!.address)
                    }

                    /*Set dueDateCategories*/
                    if(!it.data?.client!!.due_date_categories.isNullOrEmpty()){
                        dueDateCategoriesList = it.data!!.due_date_categories!!
                        selectedCategoriesList = it.data?.client!!.due_date_categories
                        for(k in 0 until it.data?.due_date_categories!!.size){
                            checkedItems.add(false)
                        }

                        for(i in 0 until selectedCategoriesList.size){
                            for(j in 0 until it.data?.due_date_categories!!.size){
                                if(selectedCategoriesList[i].equals(it.data?.due_date_categories!![j].id_due_date_category)){
                                    dueDateCategoriesNames.add(it.data?.due_date_categories!![j].category_name)
                                    checkedItems[j]=true
                                }
                            }

                        }
                        val categories: String = TextUtils.join(", ", dueDateCategoriesNames)
                        edtNotificationCategories.setText(categories)

                        edtNotificationCategories.setOnTouchListener { _, event ->
                            if (event.action == MotionEvent.ACTION_UP) {
                                val values = ArrayList<String>()
                                val key = ArrayList<String>()
                                for (i in this.dueDateCategoriesList.indices) {
                                    values.add(this.dueDateCategoriesList[i].category_name)
                                    key.add(this.dueDateCategoriesList[i].id_due_date_category)
                                }
                                val alertDialog = AlertDialog.Builder(this)
                                alertDialog.setTitle(getString(R.string.select_categories))
                                alertDialog.setMultiChoiceItems(values.toTypedArray(), checkedItems.toBooleanArray()) { _, which, isChecked ->
                                    if(isChecked){
                                        selectedCategoriesList.add(key[which])
                                        dueDateCategoriesNames.add(values[which])
                                        if(!checkedItems[which]){
                                            checkedItems[which]=true
                                        }
                                    }else{
                                        selectedCategoriesList.remove(key[which])
                                        dueDateCategoriesNames.remove(values[which])
                                        if(checkedItems[which]){
                                            checkedItems[which]=false
                                        }
                                    }
                                }
                                alertDialog.setPositiveButton("Ok") { dialogInterface, which ->
                                    val categories: String = TextUtils.join(", ", dueDateCategoriesNames)
                                    edtNotificationCategories.setText(categories)
                                    dialogInterface.dismiss()

                                }
                                alertDialog.setNegativeButton("Cancel", { dialogInterface, i -> dialogInterface.dismiss() })
                                val alert = alertDialog.create()
                                alert.setCanceledOnTouchOutside(false)
                                alert.show()
                            }
                            true
                        }
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

        /*Button Submit Click*/
        btnSubmit.setOnClickListener {
            hideKeyboard(tietAddress)
            validateInput()
        }

        /**Response of EditClient POST Api*/
        mViewModelEditClient.mModelEditClientResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    showOkFinishActivityDialog(it.message,this)
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })


    }


    private fun validateInput(){
        if(tietName.text.toString().isEmpty()){
           snackBar(getString(R.string.name_required),this)
        }else if(tietEmail.text.toString().isEmpty()or!isEmailValid(tietEmail.text.toString())){
            snackBar(getString(R.string.invalid_email_address),this)
        }else if(tietMobileNumber.text.toString().isEmpty()|| tietMobileNumber.text.toString().length!=10|| !tietMobileNumber.text.toString().isDigitsOnly()){
            snackBar(getString(R.string.invalid_number),this)
        }else if(tietAddress.text.toString().isEmpty()){
            snackBar(getString(R.string.enter_full_address),this)
        }else if(selectedCategoriesList.isNullOrEmpty()){
            snackBar(getString(R.string.select_notification_categories),this)
        }else{
            callEditClientPostApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
                tietEmail.text.toString(),tietAddress.text.toString(),selectedCategoriesList)

        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    private fun callEditClientGetApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelEditClient.editClient(idClient.toInt())
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callEditClientPostApi(name:String,mobileNumber:String,whatsapp:String,email:String,address:String,dueDateCategories:ArrayList<String>){
        ll_loading.visibility = View.VISIBLE
        val modelEditClientRequest= ModelEditClientRequest(idClient,name,mobileNumber,whatsapp,email,address,dueDateCategories)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelEditClient.editClient(idClient.toInt(),modelEditClientRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callEditClientPostApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
            tietEmail.text.toString(),tietAddress.text.toString(),selectedCategoriesList)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}