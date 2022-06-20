package com.duedatereminder.view.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.duedatereminder.R
import com.duedatereminder.databinding.ActivityNavigationDrawerBinding
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.LocalSharedPreference
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability


class NavigationDrawerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding
    private val mHeaderView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)


        binding.appBarNavigationDrawer.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_client, R.id.nav_import_client,R.id.nav_notification
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val appUpdateManager = AppUpdateManagerFactory.create(this)
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                showAppUpdateDialog()
            }
        }

        /*Set First Character*/
        if(!LocalSharedPreference.getStringValue(Constant.USER_NAME).isNullOrEmpty()) {
            val charArray = LocalSharedPreference.getStringValue(Constant.USER_NAME)!!.toCharArray()
            binding.navView.getHeaderView(0)
                .findViewById<TextView>(R.id.tvFirstLetterNav).text = charArray[0].toString()
        }

        /*Set User Name*/
        if(!LocalSharedPreference.getStringValue(Constant.USER_NAME).isNullOrEmpty()) {
            binding.navView.getHeaderView(0)
                .findViewById<TextView>(R.id.tvUserName).text =
                LocalSharedPreference.getStringValue(Constant.USER_NAME)
        }

        /*Set User EMAIL*/
        if(!LocalSharedPreference.getStringValue(Constant.USER_EMAIL).isNullOrEmpty()) {
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvUserEmail).text =
                LocalSharedPreference.getStringValue(Constant.USER_EMAIL)
        }


        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.id.nav_logout) {
                showLogoutDialog(getString(R.string.logoutAlertMessage),this)
            }
            //This is for maintaining the behavior of the Navigation view
            onNavDestinationSelected(menuItem, navController)
            //This is for closing the drawer after acting on it
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        })

    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun showAppUpdateDialog() {
        val builder =
            AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.new_version_available) as CharSequence)
        builder.setMessage(R.string.VersionUpdateMessage)
        builder.setPositiveButton(R.string.Update) { _, _ ->
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constant.appUrl + packageName)
                )
            )
        }
        builder.show()
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
            ContextExtension.callLoginActivity(this)
        }
        builder.setNegativeButton(
            R.string.cancel
        ) { _, _ ->

        }
        builder.show()
    }
}