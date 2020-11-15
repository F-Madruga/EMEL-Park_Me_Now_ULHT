package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_vehicle_form.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnVehiclesChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.VehicleFormViewModel
import java.text.SimpleDateFormat
import java.util.*


class VehicleFormFragment(var vehicleId: String? = null) : Fragment(), OnVehiclesChanged, DatePickerDialog.OnDateSetListener {

    private val TAG = VehicleFormFragment::class.java.simpleName
    private lateinit var vehicleFormViewModel: VehicleFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_form, container, false)
        vehicleFormViewModel = ViewModelProviders.of(this).get(VehicleFormViewModel::class.java)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onStart() {
        vehicleFormViewModel.registerListener(this, vehicleId)
        vehicleFormViewModel.getVehicleById()
        edit_registration_date.editText?.setOnClickListener { onEditRegistrationDateClicked() }
        super.onStart()
    }

    override fun onDestroy() {
        vehicleFormViewModel.unregisterListener()
        super.onDestroy()
    }

    override fun onVehiclesListChanged(vehicles: List<Vehicle>) {
        vehicles[0].let {
            edit_license_plate.editText?.setText(it.licensePlate)
            edit_brand.editText?.setText(it.brand)
            edit_model.editText?.setText(it.model)
            edit_registration_date.editText?.setText(SimpleDateFormat("dd/MM/yyyy").format(it.registrationDate))
        }
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

    private fun onEditRegistrationDateClicked() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            activity as Context,
            this,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        edit_registration_date.editText?.setText("$day/${month + 1}/$year")
    }

    @OnClick(R.id.button_confirm)
    fun onClickConfirm(view: View) {
        if (isFormValid()) {
            val vehicle: Vehicle = Vehicle(
                edit_license_plate.editText?.text.toString(),
                edit_brand.editText?.text.toString(),
                edit_model.editText?.text.toString(),
                edit_registration_date.editText?.text.toString()
            )
            if (this.vehicleId == null) {
                vehicleFormViewModel.addVehicle(vehicle)
            } else {
                vehicleId?.let { vehicle.uuid = it }
                vehicleFormViewModel.editVehicle(vehicle)
            }
        }
    }

    private fun isFormValid(): Boolean {
        var isValid = true
        if (edit_license_plate.editText?.text.toString() == "") {
            edit_license_plate.error = "License Plate can't be blank"
            isValid = false
        } else {
            edit_license_plate.error = null
        }
        if (edit_brand.editText?.text.toString() == "") {
            edit_brand.error = "Brand can't be blank"
            isValid = false
        } else {
            edit_brand.error = null
        }
        if (edit_model.editText?.text.toString() == "") {
            edit_model.error = "Model can't be blank"
            isValid = false
        } else {
            edit_model.error = null
        }
        if (edit_registration_date.editText?.text.toString() == "") {
            edit_registration_date.error = "Registration Date can't be blank"
            isValid = false
        } else {
            edit_registration_date.error = null
        }
        return isValid
    }

    @OnClick(R.id.button_cancel)
    fun onClickCancel(view: View) {
        activity?.onBackPressed()
    }
}
