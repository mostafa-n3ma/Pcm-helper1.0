package com.mostafan3ma.android.pcm_helper10.lines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import org.hamcrest.core.IsNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LinesViewModelTest{

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
    private lateinit var viewModel:LinesViewModel

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule= InstantTaskExecutorRule()

    @Before
    fun settingUpViewModel() {
        localDataSource = FakeDataSource(initList)
        repository = PipeLinesRepository(localDataSource, Dispatchers.Main)
        viewModel= LinesViewModel(repository)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun longClickingLine_GetLongClickedLineAndDeleteIt_LineIsDeleted()=mainCoroutineRule.runBlockingTest{
        //long Clicking line and deleting it
        viewModel.getLongClickedLine(initList[0])
        viewModel.deleteLongClickedLine()

        //after deleting the line is no longer exist
        assertThat(initList[0].name,not("line1"))
        // the list size decreased
        assertThat(initList.size,`is`(1))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun undoDeleting_DeletingLongClickedLine_GetUndoValueAndUndoDeleting()=mainCoroutineRule.runBlockingTest {
        //long Clicking line and deleting it
        viewModel.getLongClickedLine(initList[0])
        viewModel.deleteLongClickedLine()
        //undo value is true which will trigger the undo Snack Bar
        val lineDeleted: Boolean = viewModel.undo.getOrAwaitValue()
        assertThat(lineDeleted, `is`(true))
        //after deleting the line is no longer exist
        assertThat(initList[0].name, not("line1"))
        // the list size decreased
        assertThat(initList.size, `is`(1))

        //undo deleting
        viewModel.undoDeleting()
        assertThat(initList[1].name, `is`("line1"))

        //list size back to normal 2
        assertThat(initList.size, `is`(2))

        //undo compeleted and Snack Bar is Gone
        viewModel.undoCompleted()
        assertThat(viewModel.undo.getOrAwaitValue(), `is`(false))
    }


    @Test
    fun getLinesList_GettingListFromRepository(){
        val viewModelList=viewModel.linesList.getOrAwaitValue()
        val repositoryList=repository.getAllLines().getOrAwaitValue()
        assertThat(viewModelList, `is`(repositoryList))
    }


    @Test
    fun navigateToSelectedLine(){
        //selecting Line and Navigate To Details Fragment to it details
        viewModel.navigateToSelectedLine(initList[0])
        assertThat(viewModel.navigateToSelectedLine.getOrAwaitValue(), `is`(initList[0]))

        //navigation completed
        viewModel.navigateToSelectedLineComplete()
        assertThat(viewModel.navigateToSelectedLine.getOrAwaitValue(),IsNull())

    }


    @Test
    fun navigateToAddLineFragment_clickingAddLineFloatingButton(){
        //navigating to AddLineFragment
        viewModel.navigateToAddLineFragment()
        assertThat(viewModel.navigateToAddLineFragment.getOrAwaitValue(), `is`(true))

        //navigation completed
        viewModel.navigateToAddLineFragmentCompleted()
        assertThat(viewModel.navigateToAddLineFragment.getOrAwaitValue(), `is`(false))
    }

}