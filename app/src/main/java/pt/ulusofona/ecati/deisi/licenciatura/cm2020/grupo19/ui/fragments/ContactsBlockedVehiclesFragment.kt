package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_contacts_blocked_vehicles.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnVehiclesChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.NavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.ContactsBlockedVehiclesViewModel

class ContactsBlockedVehiclesFragment : Fragment(), OnVehiclesChanged {


    private val TAG = ContactsBlockedVehiclesFragment::class.java.simpleName

    private lateinit var contactsBlockedVehiclesViewModel: ContactsBlockedVehiclesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts_blocked_vehicles, container, false)
        contactsBlockedVehiclesViewModel = ViewModelProviders.of(this).get(ContactsBlockedVehiclesViewModel::class.java)
        ButterKnife.bind(this, view)
        return view
    }

    @OnClick(R.id.blocked_vehicles_phone)
    fun onPhoneClicked() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:217803131")
        this.startActivity(intent)
    }

    override fun onStart() {
        this.contactsBlockedVehiclesViewModel.registerListener(this)
        this.contactsBlockedVehiclesViewModel.getAll()
        super.onStart()
    }

    override fun onDestroy() {
        contactsBlockedVehiclesViewModel.unregisterListener()
        super.onDestroy()
    }

    override fun onVehiclesListChanged(vehicles: List<Vehicle>) {
        if (vehicles.isNotEmpty()) {
            var licensePlate: String = vehicles[0].licensePlate
            button_send_sms.setOnClickListener {
                activity?.let {
                    AlertDialog.Builder(it as Context)
                        .setTitle(getString(R.string.please_enter_license_plate))
                        .setSingleChoiceItems(vehicles.map { vehicle -> vehicle.licensePlate }.toTypedArray(), 0, DialogInterface.OnClickListener { dialog, id ->
                            licensePlate = vehicles[id].licensePlate
                        })
                        .setPositiveButton(getString(R.string.proceed), DialogInterface.OnClickListener { dialog, id ->
                            val uri = Uri.parse("smsto:3838")
                            val intent = Intent(Intent.ACTION_SENDTO, uri)
                            intent.putExtra("sms_body", "Reboque $licensePlate")
                            activity?.startActivity(intent)
                        })
                        .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, id ->
                        })
                        .show()
                }
            }
        }
        else {
            button_send_sms.setOnClickListener {
                activity?.let {
                    AlertDialog.Builder(it as Context)
                        .setTitle(getString(R.string.no_vehicles))
                        .setMessage(getString(R.string.add_vehicles))
                        .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                            activity?.supportFragmentManager?.let { fm ->
                                NavigationManager.goToAddVehicleFragment(fm)
                            }
                        })
                        .setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
                        })
                        .show()
                }
            }
        }
    }

    override fun onVehicleAdded() {
        contactsBlockedVehiclesViewModel.getAll()
    }

    override fun onVehicleDeleted() {
        contactsBlockedVehiclesViewModel.getAll()
    }

    override fun onVehicleUpdated(vehicle: Vehicle) {
        contactsBlockedVehiclesViewModel.getAll()
    }
}