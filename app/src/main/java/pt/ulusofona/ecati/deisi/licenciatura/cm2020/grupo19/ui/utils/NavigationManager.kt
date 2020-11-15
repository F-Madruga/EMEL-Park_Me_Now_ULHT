package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments.*

class NavigationManager {
    companion object {

        val path: MutableList<Fragment> = mutableListOf()

        private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
            val transition = fm.beginTransaction()
            transition.replace(R.id.frame, fragment)
            transition.commit()
        }

        fun onBackPressed(fm: FragmentManager) {
            placeFragment(fm, path.removeAt(path.size - 1))
        }

        fun goToSplashScreenFragment(fm: FragmentManager) {
            path.clear()
            placeFragment(fm, SplashScreenFragment())
        }

        fun goToParksListFragment(fm: FragmentManager) {
            path.clear()
            placeFragment(fm, ParksListFragment())
        }

        fun goToParksMapFragment(fm: FragmentManager) {
            path.clear()
            path.add(ParksListFragment())
            placeFragment(fm, ParksMapFragment())
        }

        fun goToVehiclesListFragment(fm: FragmentManager) {
            path.clear()
            path.add(ParksListFragment())
            placeFragment(fm, VehiclesListFragment())
        }

        fun goToVSettingsFragment(fm: FragmentManager) {
            path.clear()
            path.add(ParksListFragment())
            placeFragment(fm, SettingsFragment())
        }

        fun goToParkDetailsFragment(fm: FragmentManager, parkId: String) {
            path.add(ParksListFragment())
            placeFragment(fm, ParkDetailsFragment(parkId))
        }

        fun goToVehicleDetailsFragment(fm: FragmentManager, vehicleId: String) {
            path.add(VehiclesListFragment())
            placeFragment(fm, VehicleDetailsFragment(vehicleId))
        }

        fun goToAddVehicleFragment(fm: FragmentManager) {
            path.add(VehiclesListFragment())
            placeFragment(fm, VehicleFormFragment())
        }

        fun goToUpdateVehicleFragment(fm: FragmentManager, vehicleId: String) {
            path.add(VehicleDetailsFragment(vehicleId))
            placeFragment(fm, VehicleFormFragment(vehicleId))
        }

        fun gotoContactsFragment(fm: FragmentManager) {
            path.clear()
            path.add(ParksListFragment())
            placeFragment(fm, ContactsFragment())
        }
    }
}