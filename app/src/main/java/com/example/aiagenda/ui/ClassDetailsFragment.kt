package com.example.aiagenda.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentClassDetailsBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.FSize


class ClassDetailsFragment : Fragment() {
    private lateinit var binding: FragmentClassDetailsBinding

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
        showPieChart()
    }

    private fun showPieChart() {

        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(25f, "Adgadgadg"))
        entries.add(PieEntry(35f, "Bgadgadgdads"))
        entries.add(PieEntry(20f, "Cdgasagdg"))
        entries.add(PieEntry(10f, "Dadgagd"))
        entries.add(PieEntry(10f, "Eagdagd"))

        val dataSet = PieDataSet(entries, " ")
        dataSet.valueTextSize = 16f
        val colors = resources.getIntArray(R.array.chart_colors).toMutableList()
        dataSet.colors = colors

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
            legend.formSize = 12f
            legend.textSize = 12f
            legend.xEntrySpace = 20f
            invalidate()
        }
    }

}