package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class PermissionedFragment(private val requestCode: Int) : Fragment() {

    private val TAG = PermissionedFragment::class.java.simpleName

    fun onRequestPermissions(context: Context, permissions: Array<String>) {
        if (permissions.count {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            } == permissions.size) {
            onRequestPermissionsSuccess()
        } else {
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (this.requestCode == requestCode) {
            if ((grantResults.isNotEmpty()) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onRequestPermissionsSuccess()
            } else {
                onRequestPermissionsFailure()
            }
        }
        Log.i(TAG, grantResults.toString())
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    abstract fun onRequestPermissionsSuccess()

    abstract fun onRequestPermissionsFailure()
}