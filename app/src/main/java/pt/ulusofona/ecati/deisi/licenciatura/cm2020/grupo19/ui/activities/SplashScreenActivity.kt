package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils.NavigationManager

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NavigationManager.goToSplashScreenFragment(supportFragmentManager)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        }
        else {
            super.onBackPressed()
        }
    }
}
