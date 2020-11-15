package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_contacts.*
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.adapters.ContactsAdapter

class ContactsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pager.adapter = ContactsAdapter(childFragmentManager, listOf<String>(getString(R.string.service_points), getString(R.string.general_emel), getString(R.string.blocked_vehicles)))
        tab_layout.setupWithViewPager(pager)
    }

}