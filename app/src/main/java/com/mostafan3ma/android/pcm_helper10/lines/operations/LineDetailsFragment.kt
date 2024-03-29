package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
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
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.mostafan3ma.android.pcm_helper10.BuildConfig
import com.mostafan3ma.android.pcm_helper10.PcmApp
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.Utils.hideKeyboard
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import com.mostafan3ma.android.pcm_helper10.databinding.FragmentLineDetailsBinding
import com.mostafan3ma.android.pcm_helper10.lines.operations.Points.DeleteListener
import com.mostafan3ma.android.pcm_helper10.lines.operations.Points.EditListener
import com.mostafan3ma.android.pcm_helper10.lines.operations.Points.PointsAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream


class LineDetailsFragment : Fragment() {

    private lateinit var args: LineDetailsFragmentArgs
    private lateinit var pointsAdapter: PointsAdapter
    private lateinit var binding: FragmentLineDetailsBinding
    private lateinit var pointBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var editPointBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var bendBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var finishBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var noteBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var editBottomSheet: BottomSheetBehavior<LinearLayout>

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var fragmentInitializing:Boolean = true
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

        pointsAdapter = PointsAdapter(DeleteListener {
            viewModel.deletePoint(it)
            pointsAdapter.notifyDataSetChanged()
        }, EditListener {editedPoint->
            viewModel.setEditedPoint(editedPoint)
            viewModel.openEditPointSheet()
        })

        viewModel.finalLine.observe(viewLifecycleOwner, Observer {currentPipe->
            if (!fragmentInitializing){
                pointsAdapter.submitList(currentPipe.points)
            }else{
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(700).let {
                        pointsAdapter.submitList(currentPipe.points)
                        transitRecyclerView()
                    }
                    fragmentInitializing=false
                }

            }


        })
        binding.pointsRecycler.adapter = pointsAdapter
        binding.moreDetailsBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                checkExpandableCardView()
            }
        }
        setHasOptionsMenu(true)

        return binding.root
    }




    private fun transitRecyclerView() {
        TransitionManager.beginDelayedTransition(binding.recyclerCard, AutoTransition())
        binding.pointsRecycler.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            pointBottomSheet = setUpPointBottomSheet()
            editPointBottomSheet = setUpEditPointBottomSheet()
            bendBottomSheet = setUpBendBottomSheet()
            finishBottomSheet = setUpFinishBottomSheet()
            noteBottomSheet = setUpNoteBottomSheet()
            editBottomSheet = setUpEditBottomSheet()

        }
    }

    //option Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.end_point_option -> {
                viewModel.openFinishSheet()

                return true
            }
            R.id.note_option -> {
                viewModel.openNoteSheet()
                return true
            }
            R.id.share_option -> {
                checkStoragePermissionAndExportFile()
                return true
            }
            R.id.edit_option -> {
                viewModel.openEditSheet()
                return true
            }

            else -> return false

        }
    }





    //expandable  Details Card View
    private  suspend fun checkExpandableCardView() {
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


    //Bottom Sheets
    private suspend fun setUpPointBottomSheet(): BottomSheetBehavior<LinearLayout> {
        val pointBottomSheet = BottomSheetBehavior.from(binding.pointBottomSheetLayout).apply {
            isDraggable = false
        }
        viewModel.pointBottomSheetState.observe(viewLifecycleOwner, Observer {
            if (it){
                pointBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                binding.dpField.requestFocus()
                binding.fabAddPoint.visibility = View.INVISIBLE
                binding.fabAddPoint.collapse()
                checkPermissionsAndLocationSettingsAndGetLocation()
            }else{
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(pointBottomSheet)
                binding.fabAddPoint.visibility = View.VISIBLE
                locationManager.removeUpdates(locationListener)
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
                viewModel.closePointSheet()
            }
        })
        return pointBottomSheet
    }
    private suspend fun setUpBendBottomSheet(): BottomSheetBehavior<LinearLayout> {
        val bendBottomSheet = BottomSheetBehavior.from(binding.BendBottomSheetLayout).apply {
            isDraggable = false
        }
        //Bend Bottom Sheet
        viewModel.bendSheetState.observe(viewLifecycleOwner, Observer {
            when(it){
                true->{
                    bendBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.fabAddPoint.visibility = View.INVISIBLE
                    binding.fabAddPoint.collapse()
                    checkPermissionsAndLocationSettingsAndGetLocation()
                }
                false->{
                    hideKeyboard()
                    viewModel.closeBottomSheetWithDelay(bendBottomSheet)
                    binding.fabAddPoint.visibility = View.VISIBLE
                    locationManager.removeUpdates(locationListener)
                }
            }
        })
        viewModel.addBendButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.addNewBendToPipeList()
                pointsAdapter.notifyDataSetChanged()
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(bendBottomSheet)
                viewModel.addBendButtonClickedComplete()
                binding.fabAddPoint.visibility = View.VISIBLE
                viewModel.closeBendSheet()
            }
        })
        return bendBottomSheet
    }
    private suspend fun setUpEditPointBottomSheet(): BottomSheetBehavior<LinearLayout> {
        editPointBottomSheet= BottomSheetBehavior.from(binding.editPointBottomSheetLayout).apply {
            isDraggable=false
        }
        viewModel.editPointSheetState.observe(viewLifecycleOwner, Observer {isOpen->
            when(isOpen){
                true->{
                    editPointBottomSheet.state=BottomSheetBehavior.STATE_EXPANDED
                    binding.editingDpField.requestFocus()
                    binding.fabAddPoint.visibility=View.INVISIBLE
                    checkPermissionsAndLocationSettingsAndGetLocation()
                    viewModel.closeOtherSheets(viewModel.editPointSheetState)

                }
                false->{
                    hideKeyboard()
                    viewModel.closeBottomSheetWithDelay(editPointBottomSheet)
                    binding.fabAddPoint.visibility=View.VISIBLE

                }
            }
        })
        viewModel.editPointButtonClicked.observe(viewLifecycleOwner, Observer { clicked->
            if (clicked){
                viewModel.editPointButtonClickedComplete()
                pointsAdapter.notifyDataSetChanged()
                viewModel.closeEditPointSheet()
                binding.fabAddPoint.visibility=View.VISIBLE
            }

        })
        return editPointBottomSheet
    }
    private suspend fun setUpEditBottomSheet(): BottomSheetBehavior<LinearLayout> {
        editBottomSheet = BottomSheetBehavior.from(binding.editBottomSheetLayout).apply {
            isDraggable = false
        }
        //edit bottom sheet
        viewModel.editSheetState.observe(viewLifecycleOwner, Observer {isOpen->
            when(isOpen){
                true->{
                    editBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.fabAddPoint.visibility = View.INVISIBLE
                    viewModel.closeOtherSheets(viewModel.editSheetState)

                }
                false->{
                    hideKeyboard()
                    viewModel.closeBottomSheetWithDelay(editBottomSheet)
                    binding.fabAddPoint.visibility = View.VISIBLE
                }
            }

        })
        viewModel.editSaveButtonClicked.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                viewModel.editSaveButtonClickedComplete()
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(editBottomSheet)
                editBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.fabAddPoint.visibility = View.VISIBLE
                viewModel.closeEditSheet()

            }
        })
        setDropDownMenus()
        return editBottomSheet
    }
    private  suspend fun setUpNoteBottomSheet(): BottomSheetBehavior<LinearLayout> {
        noteBottomSheet = BottomSheetBehavior.from(binding.noteBottomSheetLayout).apply {
            isDraggable = false
        }
        //note bottom sheet
        viewModel.noteSheetState.observe(viewLifecycleOwner, Observer {isOpen->
            when(isOpen){
                true->{
                    noteBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.fabAddPoint.visibility = View.INVISIBLE
                    viewModel.closeOtherSheets(viewModel.noteSheetState)

                }
                false->{
                    hideKeyboard()
                    viewModel.closeBottomSheetWithDelay(noteBottomSheet)
                    binding.fabAddPoint.visibility = View.VISIBLE
                }

            }

        })
        viewModel.addNoteClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(noteBottomSheet)
                viewModel.addNoteClickedComplete()
                binding.fabAddPoint.visibility = View.VISIBLE
                viewModel.closeNoteSheet()
            }
        })
        return noteBottomSheet
    }
    private suspend fun setUpFinishBottomSheet(): BottomSheetBehavior<LinearLayout> {
        val endPointBottomSheet =
            BottomSheetBehavior.from(binding.finishBottomSheetLayout).apply {
                isDraggable = false
            }
        //Finish bottom sheet
        viewModel.finishSheetState.observe(viewLifecycleOwner, Observer { isOpen->
            when(isOpen){
                true->{
                    endPointBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.fabAddPoint.visibility = View.INVISIBLE
                    checkPermissionsAndLocationSettingsAndGetLocation()
                    viewModel.closeOtherSheets(viewModel.finishSheetState)
                }
                false->{
                    hideKeyboard()
                    viewModel.closeBottomSheetWithDelay(endPointBottomSheet)
                    binding.fabAddPoint.visibility = View.VISIBLE
                    locationManager.removeUpdates(locationListener)
                }
            }
        })
        viewModel.finishButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                hideKeyboard()
                viewModel.closeBottomSheetWithDelay(endPointBottomSheet)
                viewModel.finishButtonClickedCompleted()
                binding.fabAddPoint.visibility = View.VISIBLE
                viewModel.closeFinishSheet()
            }
        })
        return endPointBottomSheet
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
                private val typeColors: List<Int> =
                    listOf(resources.getColor(R.color.oil_txt_color)
                        , resources.getColor(R.color.water_txt_color))
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    if (view is TextView) {
                        view.setTextColor(typeColors[position])
                    }
                    return view
                }
            }
        binding.typeAutoTxt.setAdapter(typeAdapter)

    }



    // storage permissions and export file
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

//        val path: File = File(requireContext().cacheDir, "PcmRecords")
//        val newFile = File(path, "${viewModel.finalLine.value?.name}")




        var theFile: File = requireContext().cacheDir
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

        theFile = File(theFile, "${viewModel.finalLine.value?.name}.xls")
        if (theFile.exists()) {
            Log.i(TAG, "exportFile: file ${viewModel.finalLine.value?.name}.xls is exist")
        }


        try {
            val line = viewModel.finalLine.value
            val fos: FileOutputStream = FileOutputStream(theFile)
            val workbook = HSSFWorkbook()
            val sheet = workbook.createSheet("${args.selectedLine.name}")

            initLabels(sheet, line!!)
            setPointsCells(sheet, line)

            for(i in 0..10){
             sheet.setColumnWidth(i,5000)
            }
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
//        startActivity(Intent.createChooser(shareIntent, "share the ${args.selectedLine.name} file"))
////////////


        val chooser = Intent.createChooser(shareIntent, "Share File: ${args.selectedLine.name}")

        val resInfoList: List<ResolveInfo> = requireContext().packageManager
            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            requireContext().grantUriPermission(
                packageName,
                path,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        deletionPath=theFile
        startActivity(chooser)
    }
    private var deletionPath:File?=null

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

        row3.createCell(7).setCellValue("$x_coordinate,$y_coordinate")


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
                rowX.createCell(7).setCellValue(point.note)

                val x_coordinate: Double = (point.gps_x?.toDouble())?.minus(42.2) ?: 0.0
                val y_coordinate: Double = (point.gps_y?.toDouble())?.minus(481.7) ?: 0.0

                rowX.createCell(7).setCellValue("$x_coordinate,$y_coordinate")
                rowX.createCell(8).setCellValue("${point.note}")

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

                rowX.createCell(7).setCellValue("$x_coordinate,$y_coordinate")
                rowX.createCell(8).setCellValue("${point.note}")
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
            endRow.createCell(7).setCellValue("$x_coordinate,$y_coordinate")
        } else {
            endRow.createCell(7).setCellValue("")
        }
        val noteRow=sheet.createRow(rowNum+1).apply {
            createCell(0).setCellValue("Line Note")
            createCell(1).setCellValue(line.extra_note)
        }





    }





    //Location
    private fun checkPermissionsAndLocationSettingsAndGetLocation() {
        if (permissionsApproved()) {
//            checkDeviceLocationSettingsAndGetLocationUpdates()
            isLocationEnabled_UpdateLocation()
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
//            checkDeviceLocationSettingsAndGetLocationUpdates()
            isLocationEnabled_UpdateLocation()
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







private var gps_On=false

@SuppressLint("MissingPermission")
fun isLocationEnabled_UpdateLocation(){
    try {
        gps_On = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (ex: java.lang.Exception) {
    }



    if (!gps_On ) {

        Snackbar.make(
            this.requireView(),
            "Location services must be enabled to use the app", Snackbar.LENGTH_LONG
        ).setAction(android.R.string.ok) {
            startActivityForResult(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                ), REQUEST_TURN_DEVICE_LOCATION_ON
            )
        }.show()

    }else{
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0, 0.0f, locationListener
        )
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
    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== REQUEST_TURN_DEVICE_LOCATION_ON ){
            isLocationEnabled_UpdateLocation()
        }


        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        //delete the file that created for this pipe line from the cache dir when living the fragment
        deletionPath?.delete()
    }

    @VisibleForTesting
    fun expand_Fab_Menu(){
        val fab_menu=binding.fabAddPoint
        fab_menu.expand()
    }


}

private const val LOCATION_PERMISSIONS_REQUEST_CODE = 1
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 2
private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 3
private const val TAG = "LineDetailsFragment"

