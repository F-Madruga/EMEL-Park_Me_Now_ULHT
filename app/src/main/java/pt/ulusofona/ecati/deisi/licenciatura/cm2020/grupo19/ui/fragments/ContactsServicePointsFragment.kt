package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R

class ContactsServicePointsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts_service_points, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    @OnClick(R.id.service_point_central)
    fun onCentralClicked() {
        val url = Uri.parse("google.navigation:q=Campo+Grande+25,+Lisboa")
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    @OnClick(R.id.service_point_laranjeiras)
    fun onLaranjeirasClicked() {
        val url = Uri.parse("google.navigation:q=Rua+Abranches+Ferrão+10,+Lisboa")
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    @OnClick(R.id.service_point_saldanha)
    fun onSaldanhaClicked() {
        val url = Uri.parse("google.navigation:q=Mercado+31+de+Janeiro,+Rua+Engenheiro+Vieira+da+Silva,+Praça+Duque+de+Saldanha,+1050-094+Lisboa")
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

}