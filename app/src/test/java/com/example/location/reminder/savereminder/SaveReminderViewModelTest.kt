package com.example.location.reminder.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.location.reminder.R
import com.example.location.reminder.data.FakeDataSource
import com.example.location.reminder.reminders.data.dto.Result
import com.example.location.reminder.reminders.reminderslist.ReminderDataItem
import com.example.location.reminder.reminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var saveReminder: SaveReminderViewModel
    private lateinit var data: FakeDataSource

    private val item1 = ReminderDataItem("Reminder1", "Description1", "Location1", 1.0, 1.0,"1")
    private val item2 = ReminderDataItem("", "Description2", "location2", 2.0, 2.0, "2")
    private val item3 = ReminderDataItem("Reminder3", "Description3", "", 3.0, 3.0, "3")

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpViewModel(){ stopKoin()
        data = FakeDataSource()
        saveReminder = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), data)
    }

    @Test
    fun onClearsReminderLiveData(){
        saveReminder.reminderTitle.value = item1.title
        saveReminder.reminderDescription.value = item1.description
        saveReminder.reminderSelectedLocationStr.value = item1.location
        saveReminder.latitude.value = item1.latitude
        saveReminder.longitude.value = item1.longitude
        saveReminder.reminderId.value = item1.id

        saveReminder.onClear()

        assert(saveReminder.reminderTitle.value == null)
        assert(saveReminder.reminderDescription.value == null)
        assert(saveReminder.reminderSelectedLocationStr.value == null)
        assert(saveReminder.latitude.value == null)
        assert(saveReminder.longitude.value == null)
        assert(saveReminder.selectedPOI.value == null)
    }

    @Test
    fun saveReminderAndAddsReminderToDataSource() =  runTest {
        saveReminder.saveReminder(item1)

        val checkReminder = data.getReminder("1") as Result.Success

        assert(checkReminder.data.title == item1.title)
        assert(checkReminder.data.description == item1.description)
        assert(checkReminder.data.location  == item1.location)
        assert(checkReminder.data.latitude == item1.latitude)
        assert(checkReminder.data.longitude == item1.longitude)
        assert(checkReminder.data.id == item1.id)
    }

    @Test
    fun validateData_missingTitle_showSnackAndReturnFalse() {
        val valid = saveReminder.validateEnteredData(item2)
        assert(saveReminder.showSnackBarInt.value == R.string.err_enter_title)
        assert(!valid)
    }

    @Test
    fun validateData_missingLocation_showSnackAndReturnFalse(){
        val valid = saveReminder.validateEnteredData(item3)
        assert(saveReminder.showSnackBarInt.value == R.string.err_select_location)
        assert(!valid)
    }
}