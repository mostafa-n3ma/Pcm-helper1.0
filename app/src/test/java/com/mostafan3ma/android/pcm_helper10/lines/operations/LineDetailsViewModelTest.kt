package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mostafan3ma.android.pcm_helper10.Utils.MainCoroutineRule
import com.mostafan3ma.android.pcm_helper10.Utils.getOrAwaitValue
import com.mostafan3ma.android.pcm_helper10.data.source.FakeDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LineDetailsViewModelTest {

    private val initList = mutableListOf<PipeLine>(
        PipeLine(
            id = 1,
            name = "line1",
            ogm = "1",
            length = "1000",
            type = "Water",
            i_start = "1000",
            i_end = "10",
            start_point_x = "511511",
            start_point_y = "3591511",
            end_point_x = "511511",
            end_point_y = "3591511",
            start_work_date = "2/5/2022",
            end_work_date = "3/5/2022",
            work_team = "Mostafa Nema 1",
            input = "1A",
            extra_note = "note1",
            points = mutableListOf<DamagePoint>(
                DamagePoint(
                    no = 1, db = "11", depth = "1.1",
                    current1 = "111", current2 = "122",
                    gps_x = "511511", gps_y = "35911511",
                    note = "point note1", is_point = true
                ),
                DamagePoint(
                    no = 2, db = "22", depth = "2.2",
                    current1 = "211", current2 = "222",
                    gps_x = "511511", gps_y = "35911511",
                    note = "point note2", is_point = true
                ),
                DamagePoint(is_point = false),
                DamagePoint(
                    no = 3, db = "33", depth = "3.1",
                    current1 = "311", current2 = "322",
                    gps_x = "511511", gps_y = "35911511",
                    note = "point note3", is_point = true
                )
            )
        ),
        PipeLine(
            id = 2,
            name = "line2",
            ogm = "2",
            length = "2000",
            type = "oil",
            i_start = "2000",
            i_end = "20",
            start_point_x = "522522",
            start_point_y = "3591522",
            end_point_x = "522522",
            end_point_y = "3591522",
            start_work_date = "2/5/2022",
            end_work_date = "3/5/2022",
            work_team = "Mostafa Nema 2",
            input = "2A",
            extra_note = "note2",
            points = mutableListOf<DamagePoint>(
                DamagePoint(
                    no = 1, db = "11", depth = "1.1",
                    current1 = "111", current2 = "122",
                    gps_x = "511511", gps_y = "35911511",
                    note = "point note1", is_point = true
                )
            )
        )


    )
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: PipeLinesRepository
    private lateinit var viewModel: LineDetailsViewModel

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun settingUpViewModel() {
        localDataSource = FakeDataSource(initList)
        repository = PipeLinesRepository(localDataSource, Dispatchers.Main)
        viewModel = LineDetailsViewModel(repository, initList[0])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateFinalLine_Initiation_GetTheSelectedLine() = mainCoroutineRule.runBlockingTest {
        //on the initiation of viewModel finalLine==selectedLine from the Constructor which is initList[0]
        assertThat(viewModel.finalLine.getOrAwaitValue(), `is`(initList[0]))

    }


    @Test
    fun getLineDetails_DetailsFromSelectedLineToLiveValues() {
        //after starting the fragment and pass the selectedLine to the ViewModel
        //the LineDetails from the viewModel should be initialized there values
        // from the selected line to update there views in the fragment by viewBinding
        assertThat(viewModel.name.getOrAwaitValue(), `is`(initList[0].name))
        assertThat(viewModel.ogm.getOrAwaitValue(), `is`(initList[0].ogm))
        assertThat(viewModel.type.getOrAwaitValue(), `is`(initList[0].type))
        assertThat(viewModel.length.getOrAwaitValue(), `is`(initList[0].length))
        assertThat(viewModel.start_work_date.getOrAwaitValue(), `is`(initList[0].start_work_date))
        assertThat(viewModel.end_work_date.getOrAwaitValue(), `is`(initList[0].end_work_date))
        assertThat(viewModel.i_start.getOrAwaitValue(), `is`(initList[0].i_start))
        assertThat(viewModel.i_end.getOrAwaitValue(), `is`(initList[0].i_end))
        assertThat(viewModel.startPoint_x.getOrAwaitValue(), `is`(initList[0].start_point_x))
        assertThat(viewModel.startPoint_y.getOrAwaitValue(), `is`(initList[0].start_point_y))
        assertThat(viewModel.endPoint_x.getOrAwaitValue(), `is`(initList[0].end_point_x))
        assertThat(viewModel.endPoint_y.getOrAwaitValue(), `is`(initList[0].end_point_y))
        assertThat(viewModel.work_team.getOrAwaitValue(), `is`(initList[0].work_team))
        assertThat(viewModel.input.getOrAwaitValue(), `is`(initList[0].input))
        assertThat(viewModel.extra_note.getOrAwaitValue(), `is`(initList[0].extra_note))
    }


    @Test
    fun getGpsCoordinates_ProgressBarIsVisibleGpsLiveValuesEmpty_ProgressBarIsGoneGpsLiveValuesNoteEmpty() {
        //before getting the gps coordinates is available
        //progressBar is visible and gps live values is empty
        assertThat(viewModel.progressVisibility.getOrAwaitValue(), `is`(View.VISIBLE))
        assertThat(viewModel.gpsX.getOrAwaitValue(), `is`(""))
        assertThat(viewModel.gpsY.getOrAwaitValue(), `is`(""))
        assertThat(viewModel.gpsX_Y.getOrAwaitValue(), `is`(""))

        // get the gps coordinates from the location Listener...
        viewModel.getGpsCoordinates(32.5309, 45.8269, "5.0")
        //the progressBar should be gone and gps live values not empty
        assertThat(viewModel.progressVisibility.getOrAwaitValue(), `is`(View.GONE))
        assertThat(viewModel.gpsX.getOrAwaitValue(), not(""))
        assertThat(viewModel.gpsY.getOrAwaitValue(), not(""))
        assertThat(viewModel.gpsX_Y.getOrAwaitValue(), not(""))

        //converting the gps coordinates from LatLong To Utm Type
        assertThat(viewModel.gpsX.getOrAwaitValue(), `is`("577652 "))
        assertThat(viewModel.gpsY.getOrAwaitValue(), `is`("3599585"))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun addingNewPointToPointsList_GivingNewPointInfo_PointAdded() =
        mainCoroutineRule.runBlockingTest {
            //when opining new point bottom Sheet we apply it's info and then press Add button
            //the Adding process is :
            //-get the point info from the addPointBottomSheet
            // and gps coordinates from getGpsCoordinates() fun
            viewModel.dp.postValue("45")
            viewModel.depth.postValue("1.5")
            viewModel.current1.postValue("900")
            viewModel.current2.postValue("800")
            viewModel.point_note.postValue("point with db 45")
            viewModel.getGpsCoordinates(32.5309, 45.8269, "5.0")

            //checking if the values has been observed
            assertThat(viewModel.dp.getOrAwaitValue(), `is`("45"))
            assertThat(viewModel.depth.getOrAwaitValue(), `is`("1.5"))
            assertThat(viewModel.current1.getOrAwaitValue(), `is`("900"))
            assertThat(viewModel.current2.getOrAwaitValue(), `is`("800"))
            assertThat(viewModel.point_note.getOrAwaitValue(), `is`("point with db 45"))
            assertThat(viewModel.gpsX.getOrAwaitValue(), `is`("577652 "))
            assertThat(viewModel.gpsY.getOrAwaitValue(), `is`("3599585"))


            //before adding the point the viewModel must
            // get the points amount in the pointsList and
            // add the new point sequence
            //so the points amount in before adding the new point should be 3 in initList[0]
            //as the bend will not bet calculated
            var pointsAmount = 0
            initList[0].points.map { point ->
                //only points calculated
                if (point.is_point) {
                    pointsAmount += 1
                }
            }
            assertThat(pointsAmount, `is`(3))
            //and the last point in the list it's sequence (no) is 3
            var lastPointNo: Int = 0
            for (point in initList[0].points) {
                if (point.is_point && point.no > 0) {
                    lastPointNo = point.no
                }
            }

            assertThat(lastPointNo, `is`(3))
            //and pointList size should be 4
            assertThat(initList[0].points.size, `is`(4))

            //- add the point to selected Line
            viewModel.addNewPointToPipeList()
            //now we check:
            //pointsList should increases +1
            assertThat(initList[0].points.size, `is`(5))

            //now the point amount should be 4
            var pointsAmount2 = 0
            initList[0].points.map { point ->
                //only points calculated
                if (point.is_point) {
                    pointsAmount2 += 1
                }
            }
            assertThat(pointsAmount2, `is`(4))
            //and the new point sequence (no) is 4
            var lastPointNo2: Int = 0
            for (point in initList[0].points) {
                if (point.is_point && point.no > 0) {
                    lastPointNo2 = point.no
                }
            }

            assertThat(lastPointNo2, `is`(4))

            //the last point details should matches the live values from the viewModel
            val newPoint: DamagePoint = initList[0].points[initList[0].points.lastIndex]
            assertThat(newPoint.db, `is`("45"))
            //I didn't write :
            //assertThat(newPoint.db, `is`(viewModel.dp.getOrAwaitValue()))
            //because the live values from the viewModel will be reseated to "" empty
            //by the private fun resetPointLiveValues()
            assertThat(viewModel.dp.getOrAwaitValue(), `is`(""))
        }

    @ExperimentalCoroutinesApi
    @Test
    fun addingNewBendToPointsList_getBendGps_addBend() = mainCoroutineRule.runBlockingTest {
        // get the gps coordinates from the location Listener...
        viewModel.getGpsCoordinates(32.5309, 45.8269, "5.0")
        //converting the gps coordinates from LatLong To Utm Type
        assertThat(viewModel.gpsX.getOrAwaitValue(), `is`("577652 "))
        assertThat(viewModel.gpsY.getOrAwaitValue(), `is`("3599585"))

        viewModel.addNewBendToPipeList()

        //the pointList size increases +1
        assertThat(initList[0].points.size, `is`(5))
        //and the point amount should not increases or effected when adding bend
        //it should be the same form starting 3 points
        var pointsAmount = 0
        initList[0].points.map { point ->
            //only points calculated
            if (point.is_point) {
                pointsAmount += 1
            }
        }
        assertThat(pointsAmount, `is`(3))


        //now the last item in pointList should be isPoint=false
        val newbend: DamagePoint = initList[0].points[initList[0].points.lastIndex]
        assertThat(newbend.is_point, `is`(false))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteFirstPoint_PointDeleted_LastPointSequenceFixed()=mainCoroutineRule.runBlockingTest{
        //since the firstpoint will be deleted the other point sequence should be fixed
        //first delete first point and check if it deleted
        viewModel.deletePoint(initList[0].points[0])
        //check if deleted successfully
        assertThat(initList[0].points[0].db, not("11"))

        //in other side the sequence of the other points should be fixed to the new sequences after deletion
        //so now the third point and last point (no) sould be 2 not 3
        var lastPointNo: Int = 0
        for (point in initList[0].points) {
            if (point.is_point && point.no > 0) {
                lastPointNo= point.no
            }
        }

        assertThat(lastPointNo, `is`(2))


        //and the second point (no) shifted to 1
        assertThat(initList[0].points[0].no, `is`(1))
        assertThat(initList[0].points[0].db, `is`("22"))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun deleteBend_PointsSequenceStillTheSame()=mainCoroutineRule.runBlockingTest {
        //when deleting the abend from the list the point sequence should not change
        viewModel.deletePoint(initList[0].points[2])

        //last point (no) still 3
        var lastPointNo: Int = 0
        for (point in initList[0].points) {
            if (point.is_point && point.no > 0) {
                lastPointNo= point.no
            }
        }
        assertThat(lastPointNo, `is`(3))


        //after deleting the bend we can check the deletion if it successful by the list size it will decreases to 3
        assertThat(initList[0].points.size, `is`(3))
    }


    //Bottom Sheets events

    @Test
    fun pointBottomSheetState_TrueIfOpen_FalseIfClosed(){
        //default state is closed
        assertFalse(viewModel.pointBottomSheetState.getOrAwaitValue())
        //opening pointBottomSheet
        viewModel.openPointSheet()
        assertTrue(viewModel.pointBottomSheetState.getOrAwaitValue())
        //closed after clicking cancel Button
        viewModel.closePointSheet()
        assertFalse(viewModel.pointBottomSheetState.getOrAwaitValue())
        //clicking addPoint Button and completed
        viewModel.addPointButtonClicked()
        assertTrue(viewModel.addPointButtonClicked.getOrAwaitValue())
        //completed
        viewModel.addPointButtonClickedComplete()
        assertFalse(viewModel.addPointButtonClicked.getOrAwaitValue())
    }


    @Test
    fun editPointBottomSheetState_TrueIfOpen_FalseIfClosed(){
        //default state is closed
        assertFalse(viewModel.editPointSheetState.getOrAwaitValue())
        //opening pointBottomSheet
        viewModel.openEditPointSheet()
        assertTrue(viewModel.editPointSheetState.getOrAwaitValue())
        //closed after clicking cancel Button
        viewModel.closeEditPointSheet()
        assertFalse(viewModel.editPointSheetState.getOrAwaitValue())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun editPointButtonClicked_getEditedPointInfo_editPoint_closeEditPointSheet()=mainCoroutineRule.runBlockingTest {
        // getting the specific point to edit by assigning the
        // Live value _editedPoint from the listener  of the the PointsAdapter
        var editedPointForTest=initList[0].points[0]
        viewModel.setEditedPoint(editedPointForTest)
        assertThat(viewModel.editedPoint.getOrAwaitValue(), `is`(initList[0].points[0]))


        //editPointButtonClicked default value
        assertFalse(viewModel.editPointButtonClicked.getOrAwaitValue())


        //the point fields from the editPointBottomSheet are connected
        // two way binding with the editedPoint live values from the viewModel
        //so we change the editedPoints details here
        editedPointForTest.db="99"
        viewModel.setEditedPoint(editedPointForTest)
        //when editPoint button clicked:
        viewModel.editPointButtonClicked()
        //the point is edited with the new updated details and updates the line points list
        assertThat(initList[0].points[0].db, `is`(editedPointForTest.db))
        assertThat(initList[0].points[0].db, `is`("99"))

        //until now the point have the same gps before editing
        assertThat(initList[0].points[0].gps_x, `is`("511511"))

        //if the includeGps field was checked anew gps values from the fun getGpsCoordinates will be assigned to edited point
        viewModel.includeNewGps.postValue(true)
        viewModel.getGpsCoordinates(32.5309, 45.8269, "5.0")
        viewModel.editPointButtonClicked()

        //new gps applied to point
        assertThat(initList[0].points[0].gps_x, `is`("577652 "))
        assertThat(initList[0].points[0].gps_y, `is`("3599585"))

    }



    @Test
    fun bendBottomSheetState_TrueIfOpen_FalseIfClosed(){
        //default state is closed
        assertFalse(viewModel.bendSheetState.getOrAwaitValue())
        //opening pointBottomSheet
        viewModel.openBendSheet()
        assertTrue(viewModel.bendSheetState.getOrAwaitValue())
        //closed after clicking cancel Button
        viewModel.closeBendSheet()
        assertFalse(viewModel.bendSheetState.getOrAwaitValue())
        //clicking addBend  Button and completed
        viewModel.addBendButtonClicked()
        assertTrue(viewModel.addBendButtonClicked.getOrAwaitValue())
        //completed
        viewModel.addBendButtonClickedComplete()
        assertFalse(viewModel.addBendButtonClicked.getOrAwaitValue())
    }


    @Test
    fun finishSheetState_TrueIfOpen_FalseIfClosed(){
        //default state is closed
        assertFalse(viewModel.finishSheetState.getOrAwaitValue())
        //opening pointBottomSheet
        viewModel.openFinishSheet()
        assertTrue(viewModel.finishSheetState.getOrAwaitValue())
        //closed after clicking cancel Button
        viewModel.closeFinishSheet()
        assertFalse(viewModel.finishSheetState.getOrAwaitValue())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun finishButtonClicked_updateLineWithFinishingData()=mainCoroutineRule.runBlockingTest {
        //giving finished data
        viewModel.getGpsCoordinates(32.5309, 45.8269, "5.0")
        viewModel.i_end.postValue("50")
        viewModel.end_work_date.postValue("24/5/2022")

        //clicking finished Button
        viewModel.finishButtonClicked()
        // calling fun updateLineWithFinishingData will update the Line with finishing data
        assertThat(initList[0].end_point_x, `is`("577652 "))
        assertThat(initList[0].end_point_y, `is`("3599585"))
        assertThat(initList[0].i_end, `is`("50"))
        assertThat(initList[0].end_work_date, `is`("24/5/2022"))

    }



    @Test
    fun noteSheetState_TrueIfOpen_FalseIfClosed(){
        //default state is closed
        assertFalse(viewModel.noteSheetState.getOrAwaitValue())
        //opening pointBottomSheet
        viewModel.openNoteSheet()
        assertTrue(viewModel.noteSheetState.getOrAwaitValue())
        //closed after clicking cancel Button
        viewModel.closeNoteSheet()
        assertFalse(viewModel.noteSheetState.getOrAwaitValue())
    }


    @ExperimentalCoroutinesApi
    @Test
    fun addNoteClicked_addingNoteToPipeLine()=mainCoroutineRule.runBlockingTest {
        //giving note data
        viewModel.extra_note.postValue("note From Testing")

        //clicking addNote Button
        viewModel.addNoteClicked()

        //Note are added
        assertThat(initList[0].extra_note, `is`("note From Testing"))


    }


    @Test
    fun editSheetState_TrueIfOpen_FalseIfClosed(){
        //default state is closed
        assertFalse(viewModel.editSheetState.getOrAwaitValue())
        //opening pointBottomSheet
        viewModel.openEditSheet()
        assertTrue(viewModel.editSheetState.getOrAwaitValue())
        //closed after clicking cancel Button
        viewModel.closeEditSheet()
        assertFalse(viewModel.editSheetState.getOrAwaitValue())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun editSaveButtonClicked_updateLineDetails()=mainCoroutineRule.runBlockingTest {
        //giving new data to be updated in the line
        //let update the ogm and name
        viewModel.comingOgm.postValue("15")
        viewModel.name.postValue("line1 updated from Test")

        //clicking editSaveButton and calling editLine()
        viewModel.editSaveButtonClicked()


        //Line is updated with new data
        assertThat(initList[0].name, `is`("line1 updated from Test"))
        assertThat(initList[0].ogm, `is`("15"))
    }


   @Test
   fun closeOtherSheets_openFinishSheet_closingAnyOtherSheetOpened(){
       //lets open pointSheet
       viewModel.openPointSheet()
       //pointSheet are opened
       assertTrue(viewModel.pointBottomSheetState.getOrAwaitValue())

       //now lets open finishSheet and call closeOtherSheets() to any other sheet if opened
       viewModel.openFinishSheet()
       assertTrue(viewModel.finishSheetState.getOrAwaitValue())
       //passing finishSheetState to closeOtherSheets() and if it was MutableLiveData<Boolean> of finishSheetState
       //all other sheets should be closed except finishedSheet
       viewModel.closeOtherSheets(viewModel.finishSheetState)


       //now pointSheet should be closed
       assertFalse(viewModel.pointBottomSheetState.getOrAwaitValue())

   }

}