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
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.mostafan3ma.android.pcm_helper10.BuildConfig
import com.mostafan3ma.android.pcm_helper10.PcmApp
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.Utils.hideKeyboard
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentLineDetailsBinding
import com.mostafan3ma.android.pcm_helper10.lines.operations.Points.PointListener
import com.mostafan3ma.android.pcm_helper10.lines.operations.Points.PointsAdapter
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.*


class LineDetailsFragment : Fragment() {

    private lateinit var args: LineDetailsFragmentArgs
    private lateinit var pointsAdapter: PointsAdapter
    private lateinit var binding: FragmentLineDetailsBinding
    private lateinit var pointBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var bendBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var endPointBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var noteBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var editBottomSheet: BottomSheetBehavior<LinearLayout>

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    val viewModel by viewModels<LineDetailsViewModel> {
        LineDetailsViewModel.LineDetailsViewModelFactory(
            (requireContext().applicationContext as PcmApp).repository, args.selectedLine
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        args = LineDetailsFragmentArgs.fromBundle(requireArguments())


        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            viewModel.getGpsCoordinates(
                location.latitude, location.longitude,
                location.accuracy.toString()
            )
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLineDetailsBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        pointsAdapter = PointsAdapter(PointListener {
//            Toast.makeText(requireContext(), "${it.db}", Toast.LENGTH_SHORT).show()
//            viewModel.deletePoint(it)
//            pointsAdapter.notifyDataSetChanged()


        })


        viewModel.finalLine.observe(viewLifecycleOwner, Observer {
            pointsAdapter.submitList(it.points)
        })

        binding.pointsRecycler.adapter = pointsAdapter
        pointBottomSheet = setUpPointBottomSheet()
        bendBottomSheet = setUpBendBottomSheet()
        endPointBottomSheet = setUpendPointBottomSheet()
        noteBottomSheet = setUpNoteBottomSheet()
        editBottomSheet = setUpEditBottomSheet()

        //Point Bottom Sheet
        viewModel.openPointBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                pointBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                binding.dpField.requestFocus()
                viewModel.openPointBottomSheetCompleted()
                binding.fabAddPoint.visibility = View.INVISIBLE
                binding.fabAddPoint.collapse()
                checkPermissionsAndLocationSettingsAndGetLocation()
            }
        })
        viewModel.addPointButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.addNewPointToPipeList()
                pointsAdapter.notifyDataSetChanged()
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(pointBottomSheet)
                viewModel.addPointButtonClickedComplete()
                binding.fabAddPoint.visibility = View.VISIBLE
            }
        })
        viewModel.closePointBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(pointBottomSheet)
                viewModel.closePointBottomSheetCompleted()
                binding.fabAddPoint.visibility = View.VISIBLE
                locationManager.removeUpdates(locationListener)

            }
        })

        //Bend Bottom Sheet
        viewModel.openBendBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                bendBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                viewModel.openBendBottomSheetCompleted()
                binding.fabAddPoint.visibility = View.INVISIBLE
                binding.fabAddPoint.collapse()
                checkPermissionsAndLocationSettingsAndGetLocation()
            }
        })
        viewModel.addBendButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.addNewBendToPipeList()
                pointsAdapter.notifyDataSetChanged()
                bendBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                viewModel.addBendButtonClickedComplete()
                binding.fabAddPoint.visibility = View.VISIBLE
            }
        })
        viewModel.closeBendBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                bendBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                viewModel.closeBendBottomSheetCompleted()
                binding.fabAddPoint.visibility = View.VISIBLE
                locationManager.removeUpdates(locationListener)
            }
        })


        //end point bottom sheet
        viewModel.openFinishBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                endPointBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                viewModel.openFinishBottomSheetCompleted()
                binding.fabAddPoint.visibility = View.INVISIBLE
                checkPermissionsAndLocationSettingsAndGetLocation()
            }
        })
        viewModel.closeFinishBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(endPointBottomSheet)
                viewModel.closeFinishBottomSheetCompleted()
                binding.fabAddPoint.visibility = View.VISIBLE
                locationManager.removeUpdates(locationListener)
            }
        })
        viewModel.finishButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(endPointBottomSheet)
                viewModel.finishButtonClickedCompleted()
                binding.fabAddPoint.visibility = View.VISIBLE
            }
        })


        //note bottom sheet
        viewModel.openNoteBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                noteBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                viewModel.openNoteBottomSheetComplete()
                binding.fabAddPoint.visibility = View.INVISIBLE
            }
        })
        viewModel.closeNoteBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(noteBottomSheet)
                viewModel.closeNoteBottomSheetComplete()
                binding.fabAddPoint.visibility = View.VISIBLE
            }
        })
        viewModel.addNoteClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(noteBottomSheet)
                viewModel.addNoteClickedComplete()
                binding.fabAddPoint.visibility = View.VISIBLE
            }
        })


        //edit bottom sheet
        viewModel.openEditBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                editBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                viewModel.openEditBottomSheetComplete()
                binding.fabAddPoint.visibility = View.INVISIBLE
            }
        })
        viewModel.closeEditBottomSheet.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(editBottomSheet)
                viewModel.closeEditBottomSheetComplete()
                binding.fabAddPoint.visibility = View.VISIBLE
            }
        })

        viewModel.editSaveButtonClicked.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                viewModel.editSaveButtonClickedComplete()
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(editBottomSheet)
                editBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.fabAddPoint.visibility = View.VISIBLE

            }
        })
        binding.moreDetailsBtn.setOnClickListener {
            checkExpandableCardView()
        }
        setHasOptionsMenu(true)

        return binding.root
    }

     private fun checkExpandableCardView() {
        if (binding.expandableLayout.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.lineMainCardView, AutoTransition())
            binding.expandableLayout.visibility = View.VISIBLE
            binding.moreDetailsBtn.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.up_arrow,
                0,
                0,
                0
            );
            binding.moreDetailsBtn.text = "Hide Details"
        } else {
            TransitionManager.beginDelayedTransition(binding.lineMainCardView, AutoTransition())
            binding.expandableLayout.visibility = View.GONE
            binding.moreDetailsBtn.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.down_arrow,
                0,
                0,
                0
            );
            binding.moreDetailsBtn.text = "More Details"
        }
    }

    private fun setDropDownMenus() {
        val ogms = resources.getStringArray(R.array.ogms)
        val ogmAdapter = ArrayAdapter(requireContext(), R.layout.drop_dwon_item, ogms)
        binding.ogmAutoTxt.setAdapter(ogmAdapter)

        val inputs = resources.getStringArray(R.array.inputs)
        val inputsAdapter = ArrayAdapter(requireContext(), R.layout.drop_dwon_item, inputs)
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

    private fun setUpEditBottomSheet(): BottomSheetBehavior<LinearLayout> {
        editBottomSheet = BottomSheetBehavior.from(binding.editBottomSheetLayout).apply {
            isDraggable = false
        }
        setDropDownMenus()
        return editBottomSheet
    }

    private fun setUpNoteBottomSheet(): BottomSheetBehavior<LinearLayout> {
        noteBottomSheet = BottomSheetBehavior.from(binding.noteBottomSheetLayout).apply {
            isDraggable = false
        }
        return noteBottomSheet
    }

    private fun setUpendPointBottomSheet(): BottomSheetBehavior<LinearLayout> {
        val endPointBottomSheet =
            BottomSheetBehavior.from(binding.finishBottomSheetLayout).apply {
                isDraggable = false
            }
        return endPointBottomSheet
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.end_point_option -> {
                viewModel.openFinishBottomSheet()

                return true
            }
            R.id.note_option -> {
                viewModel.openNoteBottomSheet()
                return true
            }
            R.id.share_option -> {
                checkStoragePermissionAndExportFile()
                return true
            }
            R.id.edit_option -> {
                viewModel.openEditBottomSheet()
                return true
            }
            R.id.settings_option -> {
                return true
            }
            else -> return false

        }
    }

    fun storagePermissionsApproved(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkStoragePermissionAndExportFile() {
        if (storagePermissionsApproved()) {
            exportFile()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }


    private fun exportFile() {
        var theFile: File = requireContext().filesDir
        theFile = File(theFile, getString(R.string.app_name))
        if (!theFile.exists()) {
            if (theFile.mkdir()) {
                Toast.makeText(requireContext(), "Dir created successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Dir not created ", Toast.LENGTH_SHORT).show()
                return
            }
        }

        theFile = File(theFile, "${args.selectedLine.name}.xls")
        if (theFile.exists()) {
            Log.i(TAG, "exportFile: file ${args.selectedLine.name}.xls is exist")
        }


        try {
            val line = args.selectedLine
            val fos: FileOutputStream = FileOutputStream(theFile)
            val workbook = HSSFWorkbook()
            val sheet = workbook.createSheet("${args.selectedLine.name}")
            initLabels(sheet, line)
            setPointsCells(sheet, line)
            workbook.write(fos)
            fos.flush()
            fos.close()
            Toast.makeText(requireContext(), "file written successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.i(TAG, "exportFile: ${e.message}")
        }

        val path = FileProvider.getUriForFile(
            requireContext(),
            "com.mostafan3ma.android.pcm_helper10.provider",
            theFile
        )


        //mime types for files in intents
//        https://www.sitepoint.com/mime-types-complete-list/
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/vnd.ms-excel"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.putExtra(Intent.EXTRA_STREAM, path)
        startActivity(Intent.createChooser(shareIntent, "share the ${args.selectedLine.name} file"))

    }

    private fun setPointsCells(sheet: HSSFSheet, line: PipeLine) {
        var rowNum = 4
        for (point in line.points) {
            val rowX = sheet.createRow(rowNum)
            if (point.is_point) {
                rowX.createCell(0).setCellValue(point.no.toString())
                rowX.createCell(1).setCellValue(point.db)
                rowX.createCell(2).setCellValue(point.depth)
                rowX.createCell(3).setCellValue(point.current1)
                rowX.createCell(4).setCellValue(point.current2)
                rowX.createCell(5).setCellValue(point.gps_x)
                rowX.createCell(6).setCellValue(point.gps_y)

                val x_coordinate: Double = (point.gps_x?.toDouble())?.minus(42.2) ?: 0.0
                val y_coordinate: Double = (point.gps_y?.toDouble())?.minus(481.7) ?: 0.0

                rowX.createCell(7).setCellValue("$x_coordinate;$y_coordinate")
            } else {
                rowX.createCell(0).setCellValue("")
                rowX.createCell(1).setCellValue("")
                rowX.createCell(2).setCellValue("")
                rowX.createCell(3).setCellValue("")
                rowX.createCell(4).setCellValue("Bend")
                rowX.createCell(5).setCellValue(point.gps_x)
                rowX.createCell(6).setCellValue(point.gps_y)

                val x_coordinate: Double = (point.gps_x?.toDouble())?.minus(42.2) ?: 0.0
                val y_coordinate: Double = (point.gps_y?.toDouble())?.minus(481.7) ?: 0.0

                rowX.createCell(7).setCellValue("$x_coordinate;$y_coordinate")
            }
            rowNum += 1
        }
        setEndPointRow(sheet, line, rowNum)
    }

    private fun setEndPointRow(sheet: HSSFSheet, line: PipeLine, rowNum: Int) {
        val endRow = sheet.createRow(rowNum)
        endRow.createCell(4).setCellValue("End Point:")
        endRow.createCell(5).setCellValue(line.end_point_x)
        endRow.createCell(6).setCellValue(line.end_point_y)
        if (line.end_point_x!!.isNotEmpty() && line.end_point_y!!.isNotEmpty()) {
            val x_coordinate: Double = (line.end_point_x?.toDouble())?.minus(42.2) ?: 0.0
            val y_coordinate: Double = (line.end_point_y?.toDouble())?.minus(481.7) ?: 0.0
            endRow.createCell(7).setCellValue("$x_coordinate;$y_coordinate")
        } else {
            endRow.createCell(7).setCellValue("")
        }


    }

    private fun initLabels(sheet: HSSFSheet, line: PipeLine) {
        //later for styles https://poi.apache.org/components/spreadsheet/quick-guide.html
        //https://poi.apache.org/components/spreadsheet/quick-guide.html#MergedCells

        val row0 = sheet.createRow(0)
        row0.createCell(0).setCellValue("Pipe Line:")
        row0.createCell(1).setCellValue(line.name)
        row0.createCell(2).setCellValue("OGM")
        row0.createCell(3).setCellValue(line.ogm)
        row0.createCell(4).setCellValue("Type")
        row0.createCell(5).setCellValue(line.type)
        row0.createCell(6).setCellValue("Work Team")
        row0.createCell(7).setCellValue(line.work_team)

        val row1 = sheet.createRow(1)
        row1.createCell(0).setCellValue("Length:")
        row1.createCell(1).setCellValue(line.length)
        row1.createCell(2).setCellValue("WorkDate:")
        row1.createCell(3).setCellValue(line.start_work_date)
        row1.createCell(4).setCellValue("Input current:")
        row1.createCell(5).setCellValue(line.input)
        row1.createCell(6).setCellValue("Start_current:")
        row1.createCell(7).setCellValue(line.i_start)
        row1.createCell(8).setCellValue("End_current:")
        row1.createCell(9).setCellValue(line.i_end)

        val row2 = sheet.createRow(2)
        row2.createCell(0).setCellValue("No.")
        row2.createCell(1).setCellValue("Dp")
        row2.createCell(2).setCellValue("Depth")
        row2.createCell(3).setCellValue("current1")
        row2.createCell(4).setCellValue("Current2")
        row2.createCell(5).setCellValue("gps_x")
        row2.createCell(6).setCellValue("gps_y")
        row2.createCell(7).setCellValue("Map Coordinates")


        val row3 = sheet.createRow(3)
        row3.createCell(4).setCellValue("Start Point")
        row3.createCell(5).setCellValue(line.start_point_x)
        row3.createCell(6).setCellValue(line.start_point_y)

        val x_coordinate: Double = (line.start_point_x?.toDouble())?.minus(42.2) ?: 0.0
        val y_coordinate: Double = (line.start_point_y?.toDouble())?.minus(481.7) ?: 0.0

        row3.createCell(7).setCellValue("$x_coordinate;$y_coordinate")


    }


    private fun setUpBendBottomSheet(): BottomSheetBehavior<LinearLayout> {
        val bendBottomSheet = BottomSheetBehavior.from(binding.BendBottomSheetLayout).apply {
            isDraggable = false
        }
        return bendBottomSheet
    }


    private fun setUpPointBottomSheet(): BottomSheetBehavior<LinearLayout> {
        val pointBottomSheet = BottomSheetBehavior.from(binding.pointBottomSheetLayout).apply {
            isDraggable = false
        }
        return pointBottomSheet
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

        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            && grantResults[0] == PackageManager.PERMISSION_DENIED
        ) {
            Snackbar.make(
                requireView(),
                "you need to grant the storage permission to export file",
                Snackbar.LENGTH_SHORT
            ).show()
        } else {
            checkStoragePermissionAndExportFile()
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
        hideKeyboard()
    }

}

private const val LOCATION_PERMISSIONS_REQUEST_CODE = 1
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 2
private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 3

private const val TAG = "LineDetailsFragment"



