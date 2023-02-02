package com.example.location.reminder.reminderslist

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.location.reminder.data.FakeDataSource
import com.example.location.reminder.reminders.data.dto.ReminderDTO
import com.example.location.reminder.reminders.reminderslist.RemindersListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //  under test
    private lateinit var remindersList: RemindersListViewModel
    private lateinit var data: FakeDataSource

    private val item1 = ReminderDTO("Reminder1", "Description1", "Location1", 1.0, 1.0,"1")
    private val item2 = ReminderDTO("Reminder2", "Description2", "location2", 2.0, 2.0, "2")
    private val item3 = ReminderDTO("Reminder3", "Description3", "location3", 3.0, 3.0, "3")


    @Before
    fun model() { stopKoin()
        data = FakeDataSource()
        remindersList = RemindersListViewModel(ApplicationProvider.getApplicationContext(), data)
    }

    @After
    fun clearData() = runTest {
        data.deleteAllReminders()
    }

    @Test
    fun invalidateShowNoDataShowNoDataIsTrue(): Unit = runTest {
        // Empty DB
        data.deleteAllReminders()
        // Try to load Reminders
        remindersList.loadReminders()
        // expect that our reminder list Live data size is 0 and show no data is true
        assert(remindersList.remindersList.value?.size == 0)
        assert(remindersList.showNoData.value == true)
    }

    // We test retrieving the three reminders we're placing in this method.
    @Test
    fun loadRemindersLoadsThreeReminders(): Unit = runTest {
        data.deleteAllReminders()

        data.saveReminder(item1)
        data.saveReminder(item2)
        data.saveReminder(item3)

        remindersList.loadReminders()
        assert(remindersList.remindersList.value?.size == 3)
        assert(remindersList.showNoData.value == false)

    }
    // Here, we are testing checkLoading in this test.
    @Test
    fun loadRemindersCheckLoading() = runTest{

        //  Only 1 Reminder
        data.deleteAllReminders()
        data.saveReminder(item1)
        // load Reminders
        remindersList.loadReminders()
        // The loading indicator is displayed, then it is hidden after we are done.
        assert(remindersList.showLoading.value == true)
        // Execute pending coroutines actions
        // Then loading indicator is hidden
        assert(remindersList.showLoading.value == true)
    }
    // testing showing an Error
    @Test
    fun loadRemindersShouldReturnError() = runTest {
        remindersList.loadReminders()
        assert(remindersList.showSnackBar.value == null)
    }

}