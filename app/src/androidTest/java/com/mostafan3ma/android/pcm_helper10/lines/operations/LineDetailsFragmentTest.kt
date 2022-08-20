package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.view.get
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.ServiceLocator
import com.mostafan3ma.android.pcm_helper10.Utils.FakeDataSource
import com.mostafan3ma.android.pcm_helper10.Utils.MainCoroutineRule
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.android.synthetic.main.fragment_line_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LineDetailsFragmentTest {
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



    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        localDataSource = FakeDataSource(initList)
        repository = PipeLinesRepository(localDataSource, Dispatchers.Main)
        ServiceLocator.pipeLinesRepository = repository
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepo()
    }


    @Test
    fun displayDetailsFragment(){

        val bundle=LineDetailsFragmentArgs(initList[0]).toBundle()
        val fragmentScenario= launchFragmentInContainer<LineDetailsFragment>(bundle,R.style.Theme_Pcmhelper10)
        onView(withId(R.id.line_name)).check(matches(withText("line1")))
    }


    @Test
    fun clickMoreDetailsButton_displayMoreDetails(){
        val bundle=LineDetailsFragmentArgs(initList[0]).toBundle()
        val fragmentScenario= launchFragmentInContainer<LineDetailsFragment>(bundle,R.style.Theme_Pcmhelper10)
        onView(withId(R.id.more_details_btn)).perform(click())
        onView(withId(R.id.expandable_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnFabAddPoint_openFloatingActionMenu(){
        val bundle=LineDetailsFragmentArgs(initList[0]).toBundle()
        val fragmentScenario= launchFragmentInContainer<LineDetailsFragment>(bundle,R.style.Theme_Pcmhelper10)
        fragmentScenario.onFragment {
            it.fab_add_point.expand()
            val fab_menu: FloatingActionsMenu =it.fab_add_point
            fab_menu.get(1).performClick()
        }

        Thread.sleep(5000)
    }


}