package com.example.aiagenda.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setControls()
    }

    private fun setControls() {

        binding.btnClasses.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToClassesFragment()
            )
        }

        binding.btnClassbook.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToClassbookFragment()
            )
        }

        binding.btnPosts.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToPostsFragment()
            )
        }
    }

}