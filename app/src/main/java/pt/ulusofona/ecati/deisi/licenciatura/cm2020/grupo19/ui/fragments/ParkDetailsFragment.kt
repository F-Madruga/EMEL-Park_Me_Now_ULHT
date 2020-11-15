package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_park_details.*
import kotlinx.android.synthetic.main.fragment_park_details.map_view
import kotlinx.android.synthetic.main.fragment_parks_map.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnVehiclesChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.NavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.ParkDetailsViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.ParksViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.VehicleInParkViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat


class ParkDetailsFragment(var parkId: String? = null) : Fragment(), OnParksChanged,
    OnMapReadyCallback, OnVehiclesChanged {

    private var map: GoogleMap? = null
    private lateinit var parkDetailsViewModel: ParkDetailsViewModel
    private lateinit var vehicleInParkViewModel: VehicleInParkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_park_details, container, false)
        parkDetailsViewModel = ViewModelProviders.of(this).get(ParkDetailsViewModel::class.java)
        vehicleInParkViewModel = ViewModelProviders.of(this).get(VehicleInParkViewModel::class.java)
        view.map_view.onCreate(savedInstanceState)
        ButterKnife.bind(view)
        return view
    }

    override fun onStart() {
        map_view.getMapAsync(this)
        map_view.onResume()
        super.onStart()
    }

    override fun onDestroy() {
        parkDetailsViewModel.unregisterListener()
        super.onDestroy()
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        this.map?.uiSettings?.isScrollGesturesEnabled = false
        this.map?.uiSettings?.isZoomGesturesEnabled = false
        this.map?.uiSettings?.isRotateGesturesEnabled = false
        vehicleInParkViewModel.registerListener(this)
        vehicleInParkViewModel.getAll()
        parkDetailsViewModel.registerListener(this, null, null,null,   parkId)
        parkDetailsViewModel.getParkById()
    }

    override fun onParksChanged(parks: List<Park>) {
        if (parks.isNotEmpty()) {
            text_name.text = parks[0].name
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            text_distance.text = "${df.format(parks[0].distance * 0.001)} km"
            text_last_update.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parks[0].occupationDate)
            text_type.text = parks[0].type
            if (parks[0].zone != null) {
                text_zone?.text = parks[0].zone?.tariff
                text_schedule?.text = parks[0].zone?.schedule
            } else {
                linear_layout_zone?.visibility = View.INVISIBLE
                linear_layout_schedule?.visibility = View.INVISIBLE
            }
            map?.let{
                it.addMarker(
                MarkerOptions()
                    .position(LatLng(parks[0].latitude, parks[0].longitude))
                    .title(parks[0].name)
                )
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(parks[0].latitude, parks[0].longitude), 15.0f))
            }
            button_navigate.setOnClickListener {
                val url = Uri.parse("google.navigation:q=${parks[0].latitude},${parks[0].longitude}")
                val intent = Intent(Intent.ACTION_VIEW, url)
                intent.setPackage("com.google.android.apps.maps")
                activity?.startActivity(intent)
            }
        }
    }

    override fun onRequestSuccess() {
        parkDetailsViewModel.getParkById()
    }

    override fun onRequestFailure() {
        parkDetailsViewModel.getParkById()
    }

    override fun onDistanceChanged() {
        //TODO("Not yet implemented")
    }

    override fun onVehiclesListChanged(vehicles: List<Vehicle>) {
        if (vehicles.isNotEmpty()) {
            var vehicle: Vehicle = vehicles[0]
            button_park_vehicle.setOnClickListener {
                activity?.let {
                    AlertDialog.Builder(it as Context)
                        .setTitle("Please select your vehicle license plate")
                        .setSingleChoiceItems(vehicles.map { vehicle -> vehicle.licensePlate }.toTypedArray(), 0, DialogInterface.OnClickListener { dialog, id ->
                            vehicle = vehicles[id]
                        })
                        .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                            vehicle.park = parkDetailsViewModel.parks[0]
                            vehicleInParkViewModel.editVehicle(vehicle)
                        })
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        })
                        .show()
                }
            }
        }
        else {
            button_park_vehicle.setOnClickListener {
                activity?.let {
                    AlertDialog.Builder(it as Context)
                        .setTitle("Warning: You have no vehicles")
                        .setMessage("Do you wish to add one?")
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                            activity?.supportFragmentManager?.let { fm ->
                                NavigationManager.goToAddVehicleFragment(fm)
                            }
                        })
                        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        })
                        .show()
                }
            }
        }
    }

    override fun onVehicleAdded() {
        vehicleInParkViewModel.getAll()
    }

    override fun onVehicleDeleted() {
        vehicleInParkViewModel.getAll()
    }

    override fun onVehicleUpdated(vehicle: Vehicle) {
        activity?.supportFragmentManager?.let { NavigationManager.goToVehicleDetailsFragment(it, vehicle.uuid) }
    }

}
