package com.smartnotes.notepadplusplus

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartnotes.notepadplusplus.ui.noteslist.NotesListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesListActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(NotesListActivity::class.java)

    @Test
    fun activityLaunches_successfully() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun fabIsDisplayed() {
        onView(withId(R.id.fabAddNote)).check(matches(isDisplayed()))
    }

    @Test
    fun searchBarIsDisplayed() {
        onView(withId(R.id.etSearch)).check(matches(isDisplayed()))
    }

    @Test
    fun fabClick_opensAddNoteScreen() {
        onView(withId(R.id.fabAddNote)).perform(click())
        onView(withId(R.id.etTitle)).check(matches(isDisplayed()))
    }
}
