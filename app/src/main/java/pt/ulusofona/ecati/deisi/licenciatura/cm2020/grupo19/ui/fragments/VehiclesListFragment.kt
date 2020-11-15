package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_vehicle_list.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.adapters.VehiclesAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnShowVehicleDetails
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnVehiclesChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.NavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.VehiclesListViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.VehiclesViewModel

class VehiclesListFragment : Fragment(), OnVehiclesChanged, OnShowVehicleDetails {

    private lateinit var vehiclesListViewModel: VehiclesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_list, container, false)
        vehiclesListViewModel = ViewModelProviders.of(this).get(VehiclesListViewModel::class.java)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onStart() {
        vehiclesListViewModel.registerListener(this)
        vehiclesListViewModel.getAll()
        super.onStart()
    }

    override fun onDestroy() {
        vehiclesListViewModel.unregisterListener()
        super.onDestroy()
    }

    override fun onVehiclesListChanged(vehicles: List<Vehicle>) {
        if (vehicles.isEmpty()) {
            linear_layout_with_vehicles.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f)
            linear_layout_without_vehicles.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
            linear_layout_without_vehicles.visibility = View.VISIBLE
        } else {
            linear_layout_with_vehicles.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
            linear_layout_without_vehicles.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f)
            linear_layout_without_vehicles.visibility = View.INVISIBLE
        }
        vehicles.let {
            list_vehicles.layoutManager = LinearLayoutManager(activity as Context)
            list_vehicles.adapter =
                VehiclesAdapter(activity as Context, R.layout.vehicle_element, it, this)
        }
    }

    override fun onVehicleAdded() {
        vehiclesListViewModel.getAll()
    }

    override fun onVehicleDeleted() {
        vehiclesListViewModel.getAll()
    }

    override fun onVehicleUpdated(vehicle: Vehicle) {
        vehiclesListViewModel.getAll()
    }

    @OnClick(R.id.button_add)
    fun onClickAdd(view: View) {
        activity?.supportFragmentManager?.let {
            NavigationManager.goToAddVehicleFragment(it)
        }
    }

    override fun onShowVehicleDetails(vehicleId: String) {
        activity?.supportFragmentManager?.let {
            NavigationManager.goToVehicleDetailsFragment(it, vehicleId)
        }
    }
}
