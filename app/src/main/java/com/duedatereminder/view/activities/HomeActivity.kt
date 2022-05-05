package com.duedatereminder.view.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension.Companion.callLoginActivity
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.LocalSharedPreference

class HomeActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar(getString(R.string.home),false)
    }

    override fun onBackPressed() {
            if(!doubleBackToExitPressedOnce){
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this,"Please click BACK again to exit.", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false;}, 2000)
            }else{
                finish()
                return
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_account -> {
                val intent = Intent(this, EditAccountActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                showLogoutDialog(getString(R.string.logoutAlertMessage),this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog(message: String, context: Context) {
        val builder =
            AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.app_name) as CharSequence)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(
            R.string.logout
        ) { _, _ ->
            LocalSharedPreference.clearAll()
            callLoginActivity(this)
        }
        builder.setNegativeButton(
            R.string.cancel
        ) { _, _ ->

        }
        builder.show()
    }
}