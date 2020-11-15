package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.*
import kotlinx.android.synthetic.main.fragment_parks_list.*
import kotlinx.android.synthetic.main.fragment_vehicle_list.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.local.entities.Park
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.data.sensors.accelerometer.OnAccelerometerChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.adapters.ParksAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnParksChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnShowParkDetails
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.NavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.SwipeDetector
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.viewmodels.ParksListViewModel
import java.math.RoundingMode
import java.text.DecimalFormat


class ParksListFragment : Fragment(), OnParksChanged, OnShowParkDetails,
    SeekBar.OnSeekBarChangeListener, OnAccelerometerChangedListener {

    private val TAG = ParksListFragment::class.java.simpleName

    private lateinit var parksListViewModel: ParksListViewModel
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_parks_list, container, false)
        parksListViewModel = ViewModelProviders.of(this).get(ParksListViewModel::class.java)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onStart() {
        parksListViewModel.registerListener(this, null, this)
        parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
        restoreFilters()
        Log.i(TAG, activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getBoolean("ShakeFilters", true).toString())
        filter_distance.setOnSeekBarChangeListener(this)
        super.onStart()
    }

    override fun onDestroy() {
        parksListViewModel.unregisterListener()
        super.onDestroy()
    }

    override fun onParksChanged(parks: List<Park>) {
        if (parks.isEmpty()) {
            list_parks_container.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f)
            linear_layout_without_parks.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
            linear_layout_without_parks.visibility = View.VISIBLE
        } else {
            list_parks_container.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
            linear_layout_without_parks.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f)
            linear_layout_without_parks.visibility = View.INVISIBLE
        }
        parks.let {
            list_parks.layoutManager = LinearLayoutManager(activity as Context)
            list_parks.adapter = ParksAdapter(activity as Context, R.layout.park_element, it, this)
        }
        itemTouchHelper = ItemTouchHelper(
            SwipeDetector(
                list_parks.adapter as ParksAdapter,
                ContextCompat.getColor(activity as Context, R.color.emelGreen)
            )
        )
        itemTouchHelper.attachToRecyclerView(list_parks)
    }

    override fun onRequestSuccess() {
        parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
    }

    override fun onRequestFailure() {
        parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
    }

    override fun onDistanceChanged() {
        parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
    }

    override fun onShowParkDetails(parkId: String) {
        activity?.supportFragmentManager?.let {
            NavigationManager.goToParkDetailsFragment(it, parkId)
        }
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
            if (linear_layout_without_parks.visibility == View.VISIBLE) {
                linear_layout_without_parks.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4f)
            } else {
                list_parks_container.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4f)
            }
            filters_menu.visibility = View.VISIBLE

        } else if (filters_menu.visibility == View.VISIBLE) {
            if (linear_layout_without_parks.visibility == View.INVISIBLE) {
                list_parks_container.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
            } else {
                linear_layout_without_parks.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
            }
            filters_menu.visibility = View.INVISIBLE
        }
    }

    @OnCheckedChanged(R.id.filter_type_all, R.id.filter_type_estrutura, R.id.filter_type_superficie)
    fun onFilterTypeChanged(button: CompoundButton, checked: Boolean) {
        if (checked) {
            activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                ?.putString("type", button.tag.toString())
                ?.apply()

            parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
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

            parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
        }
    }

    @OnClick(R.id.button_hide_filter_menu)
    fun onClickHideFilters(view: View) {
        if (linear_layout_without_parks.visibility == View.INVISIBLE) {
            list_parks_container.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
        } else {
            linear_layout_without_parks.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f)
        }
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

        parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
    }

    override fun onAccelerometerChangedListener(acceleration: Float) {
        Log.i(TAG, "Shake detected")
        if (activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getBoolean("shakeFilters", true)!!) {
            Toast.makeText(activity as Context, "Filter restored", Toast.LENGTH_LONG)
            activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                ?.putString("availability", "All")
                ?.putString("type", "All")
                ?.putString("distance", "100")
                ?.apply()
            restoreFilters()
            parksListViewModel.getAll(activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE), compareBy<Park>{it.distance})
        }
    }
}
