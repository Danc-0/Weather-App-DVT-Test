package com.danc.weatherdvt.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.danc.weatherdvt.R
import com.danc.weatherdvt.presentation.fragments.CustomDialog
import com.danc.weatherdvt.presentation.fragments.MainFragmentDirections
import com.danc.weatherdvt.utils.NetworkConnection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CustomDialog.OnClick {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)

        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        actionBarToggle.syncState()

        transparentSystemBars()

        networkCheck()

        drawerNavigation()

    }

    private fun networkCheck() {
        if (!NetworkConnection().checkForInternet(this)) {
            CustomDialog("Failed", "Try again","Network Error. Please check your internet connection and try again", this).show(supportFragmentManager, "CustomDialog")
        }
    }


    private fun transparentSystemBars() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
    }

    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun drawerNavigation() {
        navView.setNavigationItemSelectedListener { menuItem ->
            navController.navigateUp()
            when (menuItem.itemId) {
                R.id.add_to_favourites -> {

                    true
                }

                R.id.nav_favourites -> {
                    navController.navigate(R.id.favouritePlacesFragment)
                    true
                }

                R.id.nav_places -> {
                    navController.navigate(R.id.savedPlacesFragment)
                    true
                }

                R.id.nav_settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> {
                    false
                }
            }
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun dialogContinue() {
        networkCheck()
    }

}