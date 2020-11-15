package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.battery.OnBatteryCurrentListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments.PREFERENCES_FILE
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.NavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnParksChanged, OnBatteryCurrentListener {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private val TAG = MainActivity::class.java.simpleName
    private var batteryQuestion: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupDrawerMenu()
        if (savedInstanceState == null) {
            NavigationManager.goToParksListFragment(supportFragmentManager)
        }
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onStart() {
        mainActivityViewModel.registerListener(this, null, null, this)
        mainActivityViewModel.parkMeNow()
        super.onStart()
    }

    override fun onDestroy() {
        mainActivityViewModel.unregisterListener()
        super.onDestroy()
    }

    private fun setupDrawerMenu() {
        val toogle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        nav_drawer.setNavigationItemSelectedListener(this)
        drawer.addDrawerListener(toogle)
        toogle.syncState()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (NavigationManager.path.size == 0) {
            finish()
        } else {
            NavigationManager.onBackPressed(supportFragmentManager)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_parks_list -> NavigationManager.goToParksListFragment(supportFragmentManager)
            R.id.nav_parks_map -> NavigationManager.goToParksMapFragment(supportFragmentManager)
            R.id.nav_vehicles -> NavigationManager.goToVehiclesListFragment(supportFragmentManager)
            R.id.nav_settings -> NavigationManager.goToVSettingsFragment(supportFragmentManager)
            R.id.nav_contacts -> NavigationManager.gotoContactsFragment(supportFragmentManager)
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onParksChanged(parks: List<Park>) {
        if (parks.isNotEmpty()) {
            button_park_me_now.setOnClickListener {
                val url =
                    Uri.parse("google.navigation:q=${parks[0].latitude},${parks[0].longitude}")
                val intent = Intent(Intent.ACTION_VIEW, url)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
        else {
            button_park_me_now.setOnClickListener {
                AlertDialog.Builder(this as Context)
                    .setTitle(getString(R.string.no_parks_available))
                    .setMessage(getString(R.string.cannot_proceed))
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->

                    })
                    .show()
            }
        }
    }

    override fun onRequestSuccess() {
        mainActivityViewModel.getAll(null, compareBy<Park>{it.distance})
    }

    override fun onRequestFailure() {
        mainActivityViewModel.getAll(null, compareBy<Park>{it.distance})
    }

    override fun onDistanceChanged() {
        mainActivityViewModel.getAll(null, compareBy<Park>{it.distance})
    }

    override fun onCurrentChanged(current: Double) {
        Log.i(TAG, current.toString())
        if (getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getBoolean("lowBateryDarkmode", true)!!
            && AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO && current <= 0.2 && !batteryQuestion) {
            batteryQuestion = true
            Log.i(TAG, "Ask to change to dark mode")
            AlertDialog.Builder(this as Context)
                .setTitle(getString(R.string.battery_low))
                .setMessage(getString(R.string.change_darkmode_battery))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                })
                .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
                    Log.i(TAG, "No")
                })
                .show()
        }
    }

}
