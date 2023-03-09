package com.example.aiagenda.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentSettingsBinding
import com.example.aiagenda.model.TimetableTime
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.TimetableViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory
import com.islandparadise14.mintable.model.ScheduleEntity
import java.util.*
import kotlin.collections.ArrayList

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }
    private val timetableViewModel: TimetableViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

    private val day = arrayOf("Luni", "Marti", "Miercuri", "Joi", "Vineri")
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timetable.initTable(day)

        authViewModel.getSession { user ->
            if (user != null) {
                timetableViewModel.getCourses(user)
            }
        }
        timetableViewModel.getTimetableTime()

        timetableViewModel.timetableTime.observe(viewLifecycleOwner) { time ->
            val startTime = time["startTime"]
            val startHoliday = time["startHoliday"]
            val endHoliday = time["endHoliday"]

            if (startTime != null && startHoliday != null && endHoliday != null) {
                timetableViewModel.isOdd(startTime, startHoliday, endHoliday) { idOdd ->
                    timetableViewModel.getGroupCourses("Two", idOdd)
                }
            }
        }

        timetableViewModel.timetable.observe(viewLifecycleOwner) { timetable ->
            timetable.courses.map {
                scheduleList.add(
                    ScheduleEntity(
                        originId = it.id,
                        scheduleName = it.name,
                        roomInfo = it.roomInfo,
                        scheduleDay = it.scheduleDay,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        backgroundColor = "#FDA950"
                    )
                )
            }
            binding.timetable.updateSchedules(scheduleList)
        }

        timetableViewModel.groupCourses.observe(viewLifecycleOwner) { groupCourses ->
            groupCourses.map {
                scheduleList.add(
                    ScheduleEntity(
                        originId = it.id,
                        scheduleName = it.name,
                        roomInfo = it.roomInfo,
                        scheduleDay = it.scheduleDay,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        backgroundColor = "#000000"
                    )
                )
            }
            binding.timetable.updateSchedules(scheduleList)
        }


    }


}