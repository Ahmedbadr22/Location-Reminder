package com.example.location.reminder.data

import com.example.location.reminder.reminders.data.ReminderDataSource
import com.example.location.reminder.reminders.data.dto.ReminderDTO
import com.example.location.reminder.reminders.data.dto.Result

// Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var remindersList: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (remindersList == null) return Result.Error("Data Not Exist")
        return Result.Success(remindersList!!.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remindersList?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (remindersList == null) return Result.Error("Database Error")
        if (remindersList!!.isEmpty()) return Result.Error("Database Empty")
        val reminder = remindersList!!.find { reminderDTO ->
            reminderDTO.id == id
        }

        return if (reminder != null) Result.Success(reminder)
        else Result.Error("Item With this id not found")
    }

    override suspend fun deleteAllReminders() {
        remindersList?.removeAll(remindersList!!)
    }

}