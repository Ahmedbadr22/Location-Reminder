package com.example.location.reminder.reminders.geofence

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.location.reminder.R
import com.example.location.reminder.reminders.data.ReminderDataSource
import com.google.android.gms.location.Geofence
import com.example.location.reminder.reminders.data.dto.ReminderDTO
import com.example.location.reminder.reminders.data.dto.Result
import com.example.location.reminder.reminders.reminderslist.ReminderDataItem
import com.example.location.reminder.app.utils.sendNotification
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573
        private const val TAG = "GeofenceTransitionsJobIntentService"

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }

    override fun onHandleWork(intent: Intent) {
        val geoFencingEvent = GeofencingEvent.fromIntent(intent)

        geoFencingEvent?.let {
            if (it.hasError()) {
                Log.e(TAG, it.errorCode.toString())
                return
            }

            if (it.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.v(TAG, getString(R.string.geofence_entered))
                it.triggeringGeofences?.let { it1 -> sendNotification(it1) }
            }
        }

    }

    private fun sendNotification(triggeringGeoFences: List<Geofence>) {
        triggeringGeoFences.forEach {
            val requestId = it.requestId
            val remindersLocalRepository: ReminderDataSource by inject()
            CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                val result = remindersLocalRepository.getReminder(requestId)
                if (result is Result.Success<ReminderDTO>) {
                    val reminderDTO = result.data
                    sendNotification(
                        this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                            reminderDTO.title,
                            reminderDTO.description,
                            reminderDTO.location,
                            reminderDTO.latitude,
                            reminderDTO.longitude,
                            reminderDTO.id
                        )
                    )
                }
            }
        }
    }

}
