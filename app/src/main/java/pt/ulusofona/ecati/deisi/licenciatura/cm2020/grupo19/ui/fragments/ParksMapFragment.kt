package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import butterknife.OnClick
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_parks_map.*
import kotlinx.android.synthetic.main.fragment_parks_map.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.accelerometer.OnAccelerometerChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.ParksMapViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat


class ParksMapFragment : Fragment(), OnMapReadyCallback, OnParksChanged, OnLocationChangedListener, GoogleMap.OnMyLocationButtonClickListener,
    SeekBar.OnSeekBarChangeListener, OnAccelerometerChangedListener {

    private var map: GoogleMap? = null
    private lateinit var parksMapViewModel: ParksMapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_parks_map, container, false)
        parksMapViewModel = ViewModelProviders.of(this).get(ParksMapViewModel::class.java)
        view.map_view.onCreate(savedInstanceState)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onStart() {
        map_view.getMapAsync(this)
        map_view.onResume()
        super.onStart()
    }

    override fun onDestroy() {
        parksMapViewModel.unregisterListener()
        super.onDestroy()
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        parksMapViewModel.registerListener(this, this, this)
        parksMapViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE))
        parksMapViewModel.getCurrentLocation()
        this.map?.uiSettings?.isZoomControlsEnabled = true
        this.map?.uiSettings?.isMyLocationButtonEnabled = true
        this.map?.isMyLocationEnabled = true
        this.map?.setOnMyLocationButtonClickListener{ this.onMyLocationButtonClick() }
        restoreFilters()
        filter_distance.setOnSeekBarChangeListener(this)
    }

    override fun onParksChanged(parks: List<Park>) {
        this.map?.clear()
        this.map?.let { map ->
            parks.forEach { park ->
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(park.latitude, park.longitude))
                        .title("\n" + park.name)
                        .snippet(if (park.zone?.schedule != null && park.zone?.tariff != null)
                                "   Occupation: " + "${park.occupation}" + " of " + "${park.maxCapacity}" + "   \n" +
                                "   Zone: " + park.zone!!.tariff + "   \n" +
                                "   Schedule: " + park.zone!!.schedule + "   \n" +
                                "   Last Update: " + "${SimpleDateFormat("dd/MM/yyyy HH:mm").format(park.occupationDate)}   "
                                +   "\n"
                                else
                                "   Occupation: " + "${park.occupation}" + "/" + "${park.maxCapacity}" + "   \n" +
                                "   Last Update: " + "${SimpleDateFormat("dd/MM/yyyy HH:mm").format(park.occupationDate)}   " + "\n"
                        )
                        .icon(getIcon(park.type, park.availability))
                        .alpha(
                            when(park.active) {
                                1 -> 1f
                                else -> 0.31f
                            }
                        )
                )
                map.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
                    override fun getInfoContents(p0: Marker?): View {
                        return view!!
                    }

                    override fun getInfoWindow(marker: Marker?): View {
                        val context: Context = activity as Context

                        val info = LinearLayout(context)
                        info.orientation = LinearLayout.VERTICAL

                        val title = TextView(context)
                        title.setTextColor(Color.WHITE)
                        title.setGravity(Gravity.CENTER)
                        title.setTypeface(null, Typeface.BOLD)
                        title.setText(marker!!.title)

                        val snippet = TextView(context)
                        snippet.setTextColor(Color.WHITE)
                        snippet.setText(marker.snippet)

                        info.addView(title)
                        info.addView(snippet)
                        info.setBackgroundColor(Color.GRAY)
                        return info
                    }
                })
            }
        }
    }

    private fun getIcon(type: String, availability: String): BitmapDescriptor {
        when(type) {
            "Estrutura" -> {
                val drawableStructure = ContextCompat.getDrawable(requireContext(), R.drawable.ic_structure)
                drawableStructure!!.setColorFilter(Color.RED, PorterDuff.Mode.DST_OVER)
                drawableStructure.setBounds(
                    0,
                    0,
                    (drawableStructure.intrinsicWidth * 1.5).toInt(),
                    (drawableStructure.intrinsicHeight * 1.5).toInt()
                )
                return when (availability) {
                    "Full" -> {
                        drawableStructure.setColorFilter(Color.RED, PorterDuff.Mode.DST_OVER)
                        drawableToBitmap(drawableStructure)
                    }
                    "Potentially Full" -> {
                        drawableStructure.setColorFilter(Color.rgb(255,128, 0), PorterDuff.Mode.DST_OVER)
                        drawableToBitmap(drawableStructure)
                    }
                    else -> {
                        drawableStructure.setColorFilter(Color.GREEN, PorterDuff.Mode.DST_OVER)
                        drawableToBitmap(drawableStructure)
                    }
                }
            }
            else -> {
                val drawableSurface = ContextCompat.getDrawable(requireContext(), R.drawable.ic_surface)
                drawableSurface!!.setBounds(
                    0,
                    0,
                    (drawableSurface.intrinsicWidth * 1.5).toInt(),
                    (drawableSurface.intrinsicHeight * 1.5).toInt()
                )
                return when (availability) {
                    "Full" -> {
                        drawableSurface.setColorFilter(Color.RED, PorterDuff.Mode.DST_OVER)
                        drawableToBitmap(drawableSurface)
                    }
                    "Potentially Full" -> {
                        drawableSurface.setColorFilter(Color.rgb(255,128, 0), PorterDuff.Mode.DST_OVER)
                        drawableToBitmap(drawableSurface)
                    }
                    else -> {
                        drawableSurface.setColorFilter(Color.GREEN, PorterDuff.Mode.DST_OVER)
                        drawableToBitmap(drawableSurface)
                    }
                }
            }
        }
    }

    private fun drawableToBitmap(vectorDrawable: Drawable): BitmapDescriptor {
        val bitmap = Bitmap.createBitmap(
            (vectorDrawable.intrinsicWidth * 1.5).toInt(),
            (vectorDrawable.intrinsicHeight * 1.5).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onRequestSuccess() {
        parksMapViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE))
    }

    override fun onRequestFailure() {
        parksMapViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE))
    }

    override fun onDistanceChanged() {
        parksMapViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE))
    }

    override fun onLocationChangedListener(locationResult: LocationResult) {
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude), 15.0f))
    }

    override fun onMyLocationButtonClick(): Boolean {
        parksMapViewModel.getCurrentLocation()
        return false
    }

    fun restoreFilters() {
        filter_availability.children.forEach {
            if (it.tag.toString() == activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getString("availability", "All")) {
                filter_availability.check(it.id)
            }
        }
        filter_type.children.forEach {
            if (it.tag.toString() == activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getString("type", "All")) {
                filter_type.check(it.id)
            }
        }
        filter_distance.progress = activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getString("distance", "100")?.toInt()?: 100
        if (filter_distance.progress == 100) {
            filter_distance_value.text = "All"
        }
        else {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            filter_distance_value.text = df.format(((filter_distance.progress * 50) + 50) * 0.001)
        }
    }

    @OnClick(R.id.button_filters)
    fun onClickFilters(view: View) {
        if (filters_menu.visibility == View.INVISIBLE) {
            map_view.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4f)
            filters_menu.visibility = View.VISIBLE
        } else {
            map_view.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
            filters_menu.visibility = View.INVISIBLE
        }
    }

    @OnCheckedChanged(R.id.filter_type_all, R.id.filter_type_estrutura, R.id.filter_type_superficie)
    fun onFilterTypeChanged(button: CompoundButton, checked: Boolean) {
        if (checked) {
            activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                ?.putString("type", button.tag.toString())
                ?.apply()

            parksMapViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
        }
    }

    @OnCheckedChanged(
        R.id.filter_availability_all,
        R.id.filter_availability_empty,
        R.id.filter_availability_potentially_full
    )
    fun onFilterAvailabilityChanged(button: CompoundButton, checked: Boolean) {
        if (checked) {
            activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                ?.putString("availability", button.tag.toString())
                ?.apply()

            parksMapViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
        }
    }

    @OnClick(R.id.button_hide_filter_menu)
    fun onClickHideFilters(view: View) {
        map_view.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
        filters_menu.visibility = View.INVISIBLE
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (progress == 100) {
            filter_distance_value.text = "All"
        }
        else {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            filter_distance_value.text = df.format(((progress * 50) + 50) * 0.001)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
            ?.putString("distance", (filter_distance.progress).toString())
            ?.apply()

        parksMapViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
    }

    override fun onAccelerometerChangedListener(acceleration: Float) {
        if (activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
                ?.getBoolean("shakeFilters", true)!!
        ) {
            activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                ?.putString("availability", "All")
                ?.putString("type", "All")
                ?.putString("distance", "100")
                ?.apply()
            restoreFilters()
            parksMapViewModel.getAll(
                activity?.getSharedPreferences(
                    PREFERENCES_FILE,
                    Context.MODE_PRIVATE
                ), compareBy<Park> { it.distance })
            Toast.makeText(activity as Context, "Filter restored", Toast.LENGTH_LONG)
        }
    }
}
