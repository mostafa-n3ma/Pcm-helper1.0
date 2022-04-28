package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import com.mostafan3ma.android.pcm_helper10.BuildConfig
import com.mostafan3ma.android.pcm_helper10.PcmApp
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentAddLineBinding
import java.text.SimpleDateFormat
import java.util.*


class AddLineFragment : Fragment() {
    val viewModel by viewModels<AddLineViewModel>() {
        AddLineViewModel.AddLineViewModelFactory((requireContext().applicationContext as PcmApp).repository)
    }

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            viewModel.getGpsLabels(
                location.latitude, location.longitude,
                location.accuracy.toString()
            )
        }

    }

    override fun onDetach() {
        super.onDetach()
        locationManager.removeUpdates(locationListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddLineBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController()
                    .navigate(AddLineFragmentDirections.actionAddLineFragmentToDetailsFragment(it))
                viewModel.navigateToDetailsComplete()
            }
        })

        setDropDawnMenus(binding)

        checkPermissionsAndLocationSettingsAndGetLocation()

        getCurrentDate()
        return binding.root
    }
    private fun getCurrentDate() {
        val sdf: SimpleDateFormat = SimpleDateFormat("dd/M/yyyy")
        val currentDate: String = sdf.format(Date())
        viewModel.start_work_date.postValue(currentDate)
    }

    private fun setDropDawnMenus(binding: FragmentAddLineBinding) {
        //required binding adapters
        val ogms = resources.getStringArray(R.array.ogms)
        val ogmAdapter = ArrayAdapter(requireContext(), R.layout.drop_dwon_item, ogms)
        binding.ogmAutoTxt.setAdapter(ogmAdapter)

        val inputs=resources.getStringArray(R.array.inputs)
        val inputsAdapter=ArrayAdapter(requireContext(),R.layout.drop_dwon_item,inputs)
        binding.inputAutoTxt.setAdapter(inputsAdapter)

        val types = resources.getStringArray(R.array.types)
        val typeAdapter =
            object : ArrayAdapter<String?>(requireContext(), R.layout.drop_dwon_item, types) {
                private val typeColors = listOf(Color.RED, Color.GREEN)
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    if (view is TextView) {
                        view.setBackgroundColor(typeColors[position])
                    }
                    return view
                }
            }
        binding.typeAutoTxt.setAdapter(typeAdapter)

    }

    private fun checkPermissionsAndLocationSettingsAndGetLocation() {
        if (permissionsApproved()) {
            checkDeviceLocationSettingsAndGetLocationUpdates()
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestPermissions(permissions, LOCATION_PERMISSIONS_REQUEST_CODE)
    }


    private fun permissionsApproved(): Boolean {
        val fine: Boolean = (
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ))
        val coarse: Boolean = (
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                )
                )
        return fine && coarse
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE
            && grantResults[0] == PackageManager.PERMISSION_DENIED
            && grantResults[1] == PackageManager.PERMISSION_DENIED
        ) {
            showSneakBar()
        } else {
            checkDeviceLocationSettingsAndGetLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkDeviceLocationSettingsAndGetLocationUpdates(resolve: Boolean = true) {
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = LocationRequest.QUALITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClint = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClint.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exeption ->
            if (exeption is ResolvableApiException && resolve) {
                try {
                    startIntentSenderForResult(
                        exeption.resolution.intentSender,
                        REQUEST_TURN_DEVICE_LOCATION_ON,
                        null,
                        0, 0, 0,
                        null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error geting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    this.requireView(),
                    "Location services must be enabled to use the app", Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndGetLocationUpdates()
                }.show()
            }

        }

        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0.0f, locationListener
                )

            }
        }


    }


    private fun showSneakBar() {
        Snackbar.make(
            requireView(),
            "You need to grant location permission in order to get your location",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Settings") {
                // Displays App settings screen.
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        checkDeviceLocationSettingsAndGetLocationUpdates(false)
    }
}


private const val LOCATION_PERMISSIONS_REQUEST_CODE = 1
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 2
private const val TAG = "AddLineFragment"
