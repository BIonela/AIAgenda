package com.example.aiagenda.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aiagenda.model.Course
import com.example.aiagenda.model.Timetable
import com.example.aiagenda.model.TimetableTime
import com.example.aiagenda.model.User
import com.example.aiagenda.repository.TimetableRepository
import com.example.aiagenda.util.UserDataStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TimetableViewModel(private val repository: TimetableRepository) : ViewModel() {

    private val _timetable: MutableLiveData<Timetable> =
        MutableLiveData<Timetable>()
    val timetable: LiveData<Timetable>
        get() = _timetable

    private val _groupCourses: MutableLiveData<MutableList<Course>> =
        MutableLiveData<MutableList<Course>>()
    val groupCourses: LiveData<MutableList<Course>>
        get() = _groupCourses

    private val _timetableTime: MutableLiveData<Map<String, TimetableTime>> =
        MutableLiveData<Map<String, TimetableTime>>()
    val timetableTime: LiveData<Map<String, TimetableTime>>
        get() = _timetableTime

    private val _uiState: MutableLiveData<UserDataStatus> =
        MutableLiveData<UserDataStatus>()
    val uiState: LiveData<UserDataStatus>
        get() = _uiState

    fun getCourses(user: User) {
        _uiState.postValue(UserDataStatus.LOADING)
        repository.getCourses(user, { timetable ->
            _timetable.postValue(timetable)
        }, {
            _uiState.postValue(it)
        })
    }

    fun getTimetableTime() {
        _uiState.postValue(UserDataStatus.LOADING)
        repository.getTimetableTime({ timetableTime ->
            _timetableTime.postValue(timetableTime)
        }, {
            _uiState.postValue(it)
        })
    }

    fun getGroupCourses(groupName: String, isOdd: Boolean) {
        if (groupName == "One") {
            if (isOdd) {
                repository.getGroupCourses("One", "Odd") {
                    _groupCourses.postValue(it)
                }
            } else {
                repository.getGroupCourses("One", "Even") {
                    _groupCourses.postValue(it)
                }
            }
        }
        if (groupName == "Two") {
            if (isOdd) {
                repository.getGroupCourses("Two", "Odd") {
                    _groupCourses.postValue(it)
                }
            } else {
                repository.getGroupCourses("Two", "Even") {
                    _groupCourses.postValue(it)
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun isOdd(
        startYear: TimetableTime,
        startHoliday: TimetableTime,
        endHoliday: TimetableTime,
        result: (Boolean) -> Unit
    ) {
        val startDate = LocalDate.of(startYear.year, startYear.month, startYear.dayOfMonth)
        val currentDate = LocalDate.now()

        val holidayDateStart =
            LocalDate.of(startHoliday.year, startHoliday.month, startHoliday.dayOfMonth)
        val holidayDateEnd =
            LocalDate.of(endHoliday.year, endHoliday.month, endHoliday.dayOfMonth)

        val holidayWeeks = ChronoUnit.WEEKS.between(holidayDateStart, holidayDateEnd) + 1

        val weeksBetween = ChronoUnit.WEEKS.between(startDate, currentDate)
        var currentWeek = weeksBetween + 1
        Log.e("currentWeek", currentWeek.toString())


        if (currentDate >= holidayDateStart) {
            currentWeek -= holidayWeeks
        }

        if (currentWeek.toInt() % 2 == 0) {
            result.invoke(false)
        } else {
            result.invoke(true)
        }

        Log.e("currentWeek", currentWeek.toString())
    }

}
