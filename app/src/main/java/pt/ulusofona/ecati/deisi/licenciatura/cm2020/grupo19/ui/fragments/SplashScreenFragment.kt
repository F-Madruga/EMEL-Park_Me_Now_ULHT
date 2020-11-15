package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.activities.MainActivity
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.SplashScreenViewModel

const val REQUEST_CODE = 100

class SplashScreenFragment : PermissionedFragment(REQUEST_CODE), OnParksChanged {

    private lateinit var splashScreenViewModel: SplashScreenViewModel
    private val TAG = SplashScreenFragment::class.java.simpleName
    private var location = false
    private var parks = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onRequestPermissionsSuccess() {
        FusedLocation.start(activity as Context)
        splashScreenViewModel.registerListener(this)
        splashScreenViewModel.loadData()
    }

    override fun onRequestPermissionsFailure() {
        activity?.finish()
    }

    override fun onStart() {
        super.onRequestPermissions(
            activity?.baseContext!!, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        super.onStart()
    }

    override fun onDestroy() {
        splashScreenViewModel.unregisterListener()
        super.onDestroy()
    }

    override fun onParksChanged(parks: List<Park>) {
        //TODO("Not yet implemented")
    }

    override fun onRequestSuccess() {
        if (location) {
            val intent = Intent(activity as Context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        parks = true
    }

    override fun onRequestFailure() {
        activity?.let {
            AlertDialog.Builder(it as Context)
                .setTitle(getString(R.string.no_internet_connection))
                .setMessage(getString(R.string.data_cannot_proceed))
                .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                    val intent = Intent(activity as Context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                })
                .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
                    activity?.finish()
                })
                .setCancelable(false)
                .show()
        }
    }

    override fun onDistanceChanged() {
        if (parks) {
            val intent = Intent(activity as Context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        location = true
    }
}
