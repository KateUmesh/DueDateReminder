package com.duedatereminder.view.activities

import android.R.attr.button
import android.annotation.SuppressLint
import android.content.DialogInterface
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
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showOkFinishActivityDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelAddClient
import com.google.android.material.textfield.TextInputEditText


class AddClientActivity : AppCompatActivity(), SnackBarCallback {
    private lateinit var tietName : TextInputEditText
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietMobileNumber : TextInputEditText
    private lateinit var tietWhatsappNumber : TextInputEditText
    private lateinit var tietAddress : TextInputEditText
    private lateinit var edtNotificationCategories : TextInputEditText
    private lateinit var ll_loading : LinearLayoutCompat
    var otp:String = ""
    private lateinit var mViewModelAddClient: ViewModelAddClient
    private var dueDateCategoriesList = ArrayList<DueDateCategories>()
    private var selectedCategoriesList = ArrayList<String>()
    private var dueDateCategoriesNames=ArrayList<String>()
    private var checkedItems=ArrayList<Boolean>()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        /*Toolbar*/
        toolbar(getString(R.string.add_client),true)

        /*Initialize variables*/
        tietName  = findViewById(R.id.tietName)
        tietEmail = findViewById(R.id.tietEmail)
        tietMobileNumber = findViewById(R.id.tietMobileNumber)
        tietWhatsappNumber = findViewById(R.id.tietWhatsappNumber)
        tietAddress = findViewById(R.id.tietAddress)
        edtNotificationCategories = findViewById(R.id.edtNotificationCategories)
        ll_loading = findViewById(R.id.ll_loading)
        val btnSubmit : Button = findViewById(R.id.btnSubmit)
        val nsAddClient : NestedScrollView = findViewById(R.id.nsAddClient)

        /**Initialize View Model*/
        mViewModelAddClient = ViewModelProvider(this).get(ViewModelAddClient::class.java)





        /**Call Add Client GET Api*/
        callAddClientGetApi()

        /**Response of Add Client GET Api*/
        mViewModelAddClient.mModelAddClientGetResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    nsAddClient.visibility = View.VISIBLE
                   if(!it.data?.due_date_categories.isNullOrEmpty()){
                       dueDateCategoriesList = it.data?.due_date_categories!!
                       for (i in this.dueDateCategoriesList.indices) {
                           checkedItems.add(false)
                       }

                       edtNotificationCategories.setOnTouchListener { _, event ->
                           if (event.action == MotionEvent.ACTION_UP) {
                               //selectedCategoriesList.clear()
                               //dueDateCategoriesList.clear()
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

                                   }else {
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

        /**Response of SendRegistrationOtp Api*/
        mViewModelAddClient.mModelAddClientResponse.observe(this, Observer {
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
            callAddClientApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
                tietEmail.text.toString(),tietAddress.text.toString(),selectedCategoriesList)

        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    private fun callAddClientGetApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelAddClient.addClient()
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callAddClientApi(name:String,mobileNumber:String,whatsapp:String,email:String,address:String,due_date_categories:ArrayList<String>){
        ll_loading.visibility = View.VISIBLE
        val modelAddClientRequest= ModelAddClientRequest(name,mobileNumber,whatsapp,email,address,due_date_categories)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelAddClient.addClient(modelAddClientRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callAddClientApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
            tietEmail.text.toString(),tietAddress.text.toString(),selectedCategoriesList)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}