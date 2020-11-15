package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.park_element.view.*
import org.w3c.dom.Text
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnListItemClicked
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnShowParkDetails
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnSwipeDetected
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class ParksAdapter(
    private val context: Context,
    private val layout: Int,
    private val items: List<Park>,
    private val listener: OnShowParkDetails
) : RecyclerView.Adapter<ParksAdapter.ParksListViewHolder>(), OnListItemClicked, OnSwipeDetected {

    class ParksListViewHolder(view: View, private val listener: OnListItemClicked) :
        RecyclerView.ViewHolder(view) {
        val name: TextView = view.text_name
        val type: TextView = view.text_type
        val availability: TextView = view.text_availability
        val distance: TextView = view.text_distance
        val zone: TextView? = view.text_zone
        val lastUpdate: TextView? = view.text_last_update
        val linearLayoutZone: LinearLayout? = view.linear_layout_zone

        init {
            ButterKnife.bind(this, itemView)
        }

        @OnClick
        fun onClick() {
            listener.onListItemClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParksListViewHolder {
        return ParksListViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false),
            this
        )
    }

    override fun onBindViewHolder(holder: ParksListViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.type.text = items[position].type
        holder.availability.text = items[position].availability
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        holder.distance.text = "${df.format(items[position].distance * 0.001)} km"
        if (items[position].zone != null) {
            holder.zone?.text = items[position].zone?.tariff
        } else {
            holder.linearLayoutZone?.visibility = View.INVISIBLE
        }
        holder.lastUpdate?.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(items[position].occupationDate)
    }

    override fun getItemCount() = items.size

    override fun onListItemClicked(position: Int) {
        this.listener.onShowParkDetails(items[position].parkId)
    }

    override fun onSwipeDetected(position: Int) {
        val url =
            Uri.parse("google.navigation:q=${items[position].latitude},${items[position].longitude}")
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }
}