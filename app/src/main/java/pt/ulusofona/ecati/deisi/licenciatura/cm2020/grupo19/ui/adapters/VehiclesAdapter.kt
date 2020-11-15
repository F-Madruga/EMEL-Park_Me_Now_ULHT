package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.vehicle_element.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnListItemClicked
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnShowVehicleDetails
import java.text.SimpleDateFormat

class VehiclesAdapter(
    private val context: Context,
    private val layout: Int,
    private val items: List<Vehicle>,
    private val listener: OnShowVehicleDetails
) : RecyclerView.Adapter<VehiclesAdapter.VehiclesListViewHolder>(), OnListItemClicked {

    class VehiclesListViewHolder(view: View, private val listener: OnListItemClicked) :
        RecyclerView.ViewHolder(view) {
        val licensePlate: TextView = view.text_license_plate
        val brand: TextView = view.text_brand
        val model: TextView = view.text_model
        val registrationDate: TextView = view.text_registration_date

        init {
            ButterKnife.bind(this, itemView)
        }

        @OnClick
        fun onClick() {
            listener.onListItemClicked(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclesListViewHolder {
        return VehiclesListViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false),
            this
        )
    }

    override fun onBindViewHolder(holder: VehiclesListViewHolder, position: Int) {
        holder.licensePlate.text = items[position].licensePlate
        holder.brand.text = items[position].brand
        holder.model.text = items[position].model
        holder.registrationDate.text =
            SimpleDateFormat("yyyy-MM-dd").format(items[position].registrationDate)
    }

    override fun getItemCount() = items.size

    override fun onListItemClicked(position: Int) {
        listener.onShowVehicleDetails(items[position].uuid)
    }
}