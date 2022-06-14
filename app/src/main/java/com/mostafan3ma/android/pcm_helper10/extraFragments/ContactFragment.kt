package com.mostafan3ma.android.pcm_helper10.extraFragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentContactBinding


class ContactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding=FragmentContactBinding.inflate(inflater)

        binding.facebook.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/mostafa.nema.3/")
                )
            )
        }

        binding.gmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                val email="mostafa.n3ma@gmail.com,mostafa.n3ma@gmail.com"
                val address=email.split(",".toRegex()).toTypedArray()
                data=Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,address)
                putExtra(Intent.EXTRA_SUBJECT,"about Pcm Helper App...")
            }
                startActivity(intent)

        }

        binding.whatsapp.setOnClickListener {
            try {
                val i = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://api.whatsapp.com/send?phone=" + "+96407825313141" +
                                "&text=" + "about PcmHelper app"
                    )
                )
                startActivity(i)
            }catch (e:Exception){
                val snack=Snackbar.make(requireView(),"no whatsapp installed",Snackbar.LENGTH_INDEFINITE).apply {
                    setAction("Ok",View.OnClickListener {
                        dismiss()
                    })
                }.show()

            }







        }

        binding.github.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/mostafa-n3ma")
                )
            )
        }

        binding.linkedin.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/in/mostafan3ma/")
                )
            )
        }
        return binding.root
    }

}
//
//https://github.com/mostafa-n3ma
//www.linkedin.com/in/mostafan3ma
