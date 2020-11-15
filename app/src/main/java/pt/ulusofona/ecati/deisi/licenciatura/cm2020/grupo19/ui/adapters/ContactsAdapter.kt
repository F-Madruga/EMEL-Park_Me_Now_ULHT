package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments.ContactsBlockedVehiclesFragment
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments.ContactsGeneralEmelFragment
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments.ContactsServicePointsFragment

class ContactsAdapter(fm: FragmentManager, private val pageTitles: List<String>) : FragmentPagerAdapter(fm) {

    private val fragments = listOf<Fragment>(ContactsServicePointsFragment(), ContactsGeneralEmelFragment(), ContactsBlockedVehiclesFragment())

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return pageTitles[position]
    }
}