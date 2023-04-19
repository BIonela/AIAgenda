package com.example.aiagenda.ui

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aiagenda.R
import com.example.aiagenda.adapter.CourseAdapter
import com.example.aiagenda.databinding.FragmentCoursesBinding
import com.example.aiagenda.model.PdfClass

class CoursesFragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding
    private val args: CoursesFragmentArgs by navArgs()
    private val courseAdapter = CourseAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_courses,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControls()
        setAdapter()
        setUi()
        downloadPdf()
    }

    private fun setControls() {
        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setAdapter() {
        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = courseAdapter
        }
    }

    private fun setUi() {
        if (args.schoolClass.courses.isEmpty() || args.schoolClass.courses.first() == PdfClass()) {
            binding.apply {
                tvNoCourse.visibility = View.VISIBLE
                ivNoCourse.visibility = View.VISIBLE
                rvCourses.visibility = View.GONE
            }
        } else {
            courseAdapter.submitList(args.schoolClass.courses)
            binding.apply {
                tvNoCourse.visibility = View.GONE
                ivNoCourse.visibility = View.GONE
                rvCourses.visibility = View.VISIBLE
            }
        }
    }

    private fun downloadPdf() {
        courseAdapter.onItemClick = { course ->

            Toast.makeText(
                requireContext(),
                getString(R.string.file_downloading),
                Toast.LENGTH_SHORT
            ).show()

            val downloadManager =
                requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val url = course.url
            val request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(course.name)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${course.name}.pdf"
            )
            downloadManager.enqueue(request)
        }
    }
}