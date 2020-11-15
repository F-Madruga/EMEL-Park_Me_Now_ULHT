package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_vehicle_details.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnVehiclesChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.NavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.VehicleDetailsViewModel
import java.text.SimpleDateFormat


class VehicleDetailsFragment(var vehicleId: String? = null) : Fragment(), OnVehiclesChanged {

    private lateinit var vehicleDetailsViewModel: VehicleDetailsViewModel
    private val TAG = VehicleDetailsFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_details, container, false)
        vehicleDetailsViewModel = ViewModelProviders.of(this).get(VehicleDetailsViewModel::class.java)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onStart() {
        vehicleDetailsViewModel.registerListener(this, this.vehicleId)
        vehicleDetailsViewModel.getVehicleById()
        super.onStart()
    }

    @OnClick(R.id.button_delete)
    fun onClickDelete(view: View) {
        activity?.let {
            AlertDialog.Builder(it as Context)
                .setTitle(getString(R.string.you_are_deleting_a_vehicle))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->

                })
                .setNegativeButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                    vehicleDetailsViewModel.onClickDelete()
                    activity?.onBackPressed()
                })
                .show()
        }
    }

    @OnClick(R.id.button_edit)
    fun onClickEdit(view: View) {
        activity?.supportFragmentManager?.let { fm ->
            vehicleDetailsViewModel.vehicleId?.let { id ->
                NavigationManager.goToUpdateVehicleFragment(fm,
                    id
                )
            }
        }
    }

    override fun onVehiclesListChanged(vehicles: List<Vehicle>) {
        if (vehicles.isNotEmpty()) {
            vehicles[0].let {
                text_license_plate.text = it.licensePlate
                text_brand.text = it.brand
                text_model.text = it.model
                text_registration_date.text =
                    SimpleDateFormat("yyyy-MM-dd").format(it.registrationDate)
                button_send_sms.setOnClickListener { view ->
                    val uri = Uri.parse("smsto:3838")
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    intent.putExtra("sms_body", "Reboque ${it.licensePlate}")
                    activity?.startActivity(intent)
                }
                if (it.park == null) {

                    park_container.layoutParams =
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f)
                    details_container.weightSum = details_container.weightSum - park_container.tag.toString().toInt()
                }
                else {
                    button_unpark.setOnClickListener { view ->
                        val vehicle = it
                        vehicle.park = null
                        vehicleDetailsViewModel.editVehicle(vehicle)
                        activity?.supportFragmentManager?.let { it1 ->
                            NavigationManager.goToParksListFragment(
                                it1
                            )
                        }
                    }
                    button_check_park.setOnClickListener { view ->
                        activity?.supportFragmentManager?.let { it1 ->
                            it.park?.parkId?.let { it2 ->
                                NavigationManager.goToParkDetailsFragment(
                                    it1, it2
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @OnClick(R.id.button_send_sms)
    fun onClick(view: View) {

    }

    override fun onVehicleAdded() {
        activity?.onBackPressed()
    }

    override fun onVehicleDeleted() {
        activity?.onBackPressed()
    }

    override fun onVehicleUpdated(vehicle: Vehicle) {
        activity?.onBackPressed()
    }
}
