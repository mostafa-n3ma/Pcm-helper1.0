package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mostafan3ma.android.pcm_helper10.Utils.MainCoroutineRule
import com.mostafan3ma.android.pcm_helper10.Utils.getOrAwaitValue
import com.mostafan3ma.android.pcm_helper10.data.source.FakeDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import com.mostafan3ma.android.pcm_helper10.lines.LinesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class AddLineViewModelTest {

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
    private lateinit var viewModel: AddLineViewModel

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun settingUpViewModel() {
        localDataSource = FakeDataSource(initList)
        repository = PipeLinesRepository(localDataSource, Dispatchers.Main)
        viewModel = AddLineViewModel(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertingLineData_AddLineToDataBase_NavigateToLineDetailsFragment() =
        mainCoroutineRule.runBlockingTest {
//        viewModel Properties like name,ogm,type..etc  are connected two way Binding with it related views
//        so there values are observed directly to the viewModel

            //when clicking on addLine Button the values must be observed already from the views
            viewModel.name.value = "new Line"
            viewModel.ogm.value = "1"
            viewModel.type.value = "Oil"
            viewModel.length.value = "1000"
            viewModel.start_work_date.value = "1/5/2022"
            viewModel.i_start.value = "999"
            viewModel.work_team.value = "Mostafa"
            viewModel.input.value = "1A"
            viewModel.extra_note.value = "Note1"


//        when clicking AddLine Button line are added to database and navigate to DetailsFragment with new line
            viewModel.addLineToDatabaseAndNavigateToDetails()


            //then the line will be the last line added to the database
//        we get the lastPipe from the database and navigate to its details
            assertThat(viewModel.navigateToDetails.getOrAwaitValue()?.name, `is`("new Line"))
            assertThat(initList.size, `is`(3))
            assertThat(repository.getLastPipeLine()?.name, `is`("new Line"))
        }

    @Test
    fun getGpsLabels() {
        //before getting the coordinates the progress bar is visible
        assertThat(viewModel.progressVisibility.getOrAwaitValue(), `is`(View.VISIBLE))

        //when the locationListener gets the Location Coordinates it triggers this fun and sets the gps_x and gps_y values
        //it also converts the Coordinates from latLong to Utm type
        viewModel.getGpsLabels(32.5309,45.8269,"5.0")

        val startX=viewModel.startPoint_x.getOrAwaitValue()
        val startY=viewModel.startPoint_y.getOrAwaitValue()


        //gps values after converting them
        assertThat(viewModel.startPoint_x.getOrAwaitValue(), `is`("577652 "))
        assertThat(viewModel.startPoint_y.getOrAwaitValue(), `is`("3599585"))

        //accuracy value is set
        assertThat(viewModel.accuracy.getOrAwaitValue(), `is`("accuracy: 5.0"))
        //progressBar is Gone
        assertThat(viewModel.progressVisibility.getOrAwaitValue(), `is`(View.GONE))


    }
}