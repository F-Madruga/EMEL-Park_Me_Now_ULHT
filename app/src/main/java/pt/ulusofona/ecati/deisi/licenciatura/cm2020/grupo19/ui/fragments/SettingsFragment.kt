package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import butterknife.OnCheckedChanged
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R

const val PREFERENCES_FILE = "PREFERENCES_FILE"

class SettingsFragment : PreferenceFragmentCompat() {

    private val TAG = SettingsFragment::class.java.simpleName

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        this.preferenceScreen.findPreference<SwitchPreference>("darkMode")?.isChecked = activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getInt("applicationTheme", AppCompatDelegate.MODE_NIGHT_NO) == AppCompatDelegate.MODE_NIGHT_YES
        this.preferenceScreen.findPreference<SwitchPreference>("lowBateryDarkmode")?.isEnabled = activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getInt("applicationTheme", AppCompatDelegate.MODE_NIGHT_NO) != AppCompatDelegate.MODE_NIGHT_YES
        this.preferenceScreen.findPreference<SwitchPreference>("darkModeAtNight")?.isEnabled = activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getInt("applicationTheme", AppCompatDelegate.MODE_NIGHT_NO) != AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when(preference?.key) {
            "shakeFilters" -> {
                activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                    ?.putBoolean("shakeFilters", this.findPreference<SwitchPreference>("shakeFilters")?.isChecked?:true)
                    ?.apply()
            }
            "darkModeAtNight" -> {
                activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                    ?.putInt("applicationTheme", if (this.findPreference<SwitchPreference>("darkModeAtNight")?.isChecked?:true) {
                        AppCompatDelegate.MODE_NIGHT_AUTO
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    })
                    ?.apply()

                activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getInt(
                    "applicationTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)?.let {
                    AppCompatDelegate.setDefaultNightMode(it)
                }
            }
            "darkMode" -> {
                activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                    ?.putInt("applicationTheme", if (this.findPreference<SwitchPreference>("darkMode")?.isChecked?:true) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    })
                    ?.apply()

                activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.getInt(
                    "applicationTheme", AppCompatDelegate.MODE_NIGHT_NO)?.let {
                    AppCompatDelegate.setDefaultNightMode(it)
                }
            }
            "lowBateryDarkmode" -> {
                activity?.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)?.edit()
                    ?.putBoolean("lowBateryDarkmode", this.findPreference<SwitchPreference>("lowBateryDarkmode")?.isChecked?:true)
                    ?.apply()
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onStart() {
        super.onStart()
    }

}