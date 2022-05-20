package com.mostafan3ma.android.pcm_helper10.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.mostafan3ma.android.pcm_helper10.Utils.MainCoroutineRule
import com.mostafan3ma.android.pcm_helper10.Utils.getOrAwaitValue
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PipeLinesRepositoryTest {


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

    private val newLine3=PipeLine(
        id = 3,
        name = "line3",
        ogm = "3",
        length = "3000",
        type = "Oil",
        i_start = "3000",
        i_end = "30",
        start_point_x = "533533",
        start_point_y = "3593533",
        end_point_x = "533533",
        end_point_y = "3593533",
        start_work_date = "3/5/2022",
        end_work_date = "33/5/2022",
        work_team = "Mostafa Nema 3",
        input = "3A",
        extra_note = "note3",
        points = mutableListOf<DamagePoint>(
            DamagePoint(
                no = 1, db = "33", depth = "3.1",
                current1 = "311", current2 = "322",
                gps_x = "533533", gps_y = "3593533",
                note = "point note3", is_point = true
            )

        )
    )
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: PipeLinesRepository


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule=InstantTaskExecutorRule()

    @Before
    fun createRepository() {
        localDataSource = FakeDataSource(initList)
        repository = PipeLinesRepository(localDataSource, Dispatchers.Main)
    }

    //observing Pipe list
    @Test
    fun getAllLines_getLiveDataForAllLines(){
        val liveList: List<PipeLine> =repository.getAllLines().getOrAwaitValue()
        assertThat(initList,IsEqual(liveList))
    }



    @ExperimentalCoroutinesApi
    @Test
    fun getPipLine1_givingPipeId_getSpecificPipe()=mainCoroutineRule.runBlockingTest{
        val pipe1=repository.getPipeLine(1)
    assertThat(pipe1?.id,IsEqual(1))
    assertThat(pipe1?.name,IsEqual("line1"))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getLastPipeLine_gettingLastPipeLine()=mainCoroutineRule.runBlockingTest {
        val lastPipeLine=repository.getLastPipeLine()
        assertThat(lastPipeLine?.name,IsEqual("line2"))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clearAllLines_EmptyPipeList()=mainCoroutineRule.runBlockingTest {
        repository.clearAllLines()
        assertThat(initList, IsEmptyCollection<PipeLine>())
        assertThat(initList.size,IsEqual(0))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun deleteLine1()=mainCoroutineRule.runBlockingTest{
        repository.deleteLine(1)
        //after deleting line1  index 0 of the initList is line2
        assertThat(initList[0].name,IsEqual("line2"))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun insertLine_insertingNewLine_gettingTheNewLine_initListSizeIs3()=mainCoroutineRule.runBlockingTest {
        repository.insertLine(newLine3)
        assertThat(initList.lastIndex,IsEqual(2))
        assertThat(initList.size,IsEqual(3))
        assertThat(initList[2].name,IsEqual("line3"))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateLine_UpdateLine1_gettingUpdatedData()=mainCoroutineRule.runBlockingTest {
        val line1=initList[0]
        line1.name="updated Line 1"
        repository.updateLine(line1)
        assertThat(initList[0].name,IsEqual("updated Line 1"))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun updatePointsList_updatingLine1FirstPoint_gettingUpdatedPoint()=mainCoroutineRule.runBlockingTest {
        val newList=mutableListOf<DamagePoint>(
            DamagePoint(
                no = 1, db = "99", depth = "9.1",
                current1 = "111", current2 = "122",
                gps_x = "511511", gps_y = "35911511",
                note = "updated point note1", is_point = true
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
        repository.updatePointsList(1,newList)
        assertThat(initList[0].points[0].db,IsEqual("99"))
        assertThat(initList[0].points[0].note,IsEqual("updated point note1"))
    }



}