package com.example.aiagenda.ui

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentClassDetailsBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.io.File

class ClassDetailsFragment : Fragment() {
    private lateinit var binding: FragmentClassDetailsBinding
    private val args: ClassDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_class_details,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setControls()
        setInfo()
        showPieChart()
    }

    private fun setControls() {
        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCourses.setOnClickListener {
            findNavController().navigate(
                ClassDetailsFragmentDirections.actionClassDetailsFragmentToCoursesFragment(args.schoolClass)
            )

//            val downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            val url = "https://firebasestorage.googleapis.com/v0/b/aiagenda-5241a.appspot.com/o/courses%2FTehnici%20de%20securizare%20a%20datelor%20si%20programelor%2F1.%20SSI%20-%20Introducere%20-%20Copy.pdf?alt=media&token=af067045-0f9f-4b88-b088-ff4851513c61"
//            val request = DownloadManager.Request(Uri.parse(url))
//            request.setTitle("CURS1") // setați numele dorit pentru fișier
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "curs1.pdf") // setați calea dorită pentru descărcare
//
//            downloadManager.enqueue(request)


        }
    }

    private fun setInfo() {
        binding.tvClassName.text = args.schoolClass.shortName
        binding.tvExamType.text = getString(
            R.string.exam_type,
            args.schoolClass.exam.replaceFirstChar { value -> value.titlecase() }
        )
        binding.tvCredits.text = getString(
            R.string.credits,
            args.schoolClass.credits.toString()
        )
        binding.tvTeacherName.text = args.schoolClass.teacher["name"]
        binding.tvTeacherEmail.text = args.schoolClass.teacher["email"]

    }

    private fun showPieChart() {

        val entries = mutableListOf<PieEntry>()
        args.schoolClass.percentage.entries.map {
            entries.add(
                PieEntry(
                    it.value.toFloat(),
                    it.key.replaceFirstChar { value -> value.titlecase() })
            )
        }
        val dataSet = PieDataSet(entries, " ")
        dataSet.valueTextSize = 16f
        dataSet.colors = resources.getIntArray(R.array.chart_colors).toMutableList()
        val data = PieData(dataSet)

        binding.pieChart.apply {
            setData(data)
            holeRadius = 0f
            transparentCircleRadius = 0f
            description.isEnabled = false
            setDrawEntryLabels(false)
            legend.isEnabled = true
            legend.isWordWrapEnabled = true
            legend.textColor = Color.BLACK
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.formSize = 14f
            legend.textSize = 14f
            legend.xEntrySpace = 20f
            invalidate()
        }
    }

}