package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.R

class ContactsGeneralEmelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts_general_emel, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    @OnClick(R.id.button_general_emel_phone)
    fun onPhoneClicked() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:211163060")
        this.startActivity(intent)
    }

}