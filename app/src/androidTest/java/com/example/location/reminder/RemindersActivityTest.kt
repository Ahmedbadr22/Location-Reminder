package com.example.location.reminder

import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.location.reminder.authentication.data.repository.AuthRepository
import com.example.location.reminder.authentication.data.repository.AuthRepositoryImpl
import com.example.location.reminder.authentication.data.source.firebase.AuthDataSource
import com.example.location.reminder.authentication.ui.viewModel.AuthViewModel
import com.example.location.reminder.reminders.RemindersActivity
import com.example.location.reminder.reminders.data.ReminderDataSource
import com.example.location.reminder.reminders.data.local.LocalDB
import com.example.location.reminder.reminders.data.local.RemindersLocalRepository
import com.example.location.reminder.reminders.reminderslist.RemindersListViewModel
import com.example.location.reminder.reminders.savereminder.SaveReminderViewModel
import com.example.location.reminder.util.DataBindingIdlingResource
import com.example.location.reminder.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
@LargeTest
class RemindersActivityTest :
    AutoCloseKoinTest() {

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application

    private val dataBindingResource = DataBindingIdlingResource()

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
            singleOf(::AuthDataSource)

            singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

            viewModelOf(::AuthViewModel)
        }

        startKoin {
            modules(listOf(myModule))
        }

        repository = get()

        runBlocking {
            repository.deleteAllReminders()
        }
    }


    @Test
    fun showSnackAndEnterTitle(){
        // GIVEN - Launch Reminders Activity
        val scenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingResource.monitorActivity(scenario)
        // WHEN - click on add reminder and try to save the reminder without giving any inputs
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.saveReminder)).perform(click())
        // THEN - expect we have a SnackBar displaying err_enter_title
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.err_enter_title)))

        scenario.close()
    }
}
