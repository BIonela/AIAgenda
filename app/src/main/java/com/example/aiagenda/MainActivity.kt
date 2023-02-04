package com.example.aiagenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aiagenda.databinding.ActivityMainBinding
import com.example.aiagenda.databinding.FragmentSettingsBinding
import com.example.aiagenda.ui.HomeFragment
import com.example.aiagenda.ui.MenuFragment
import com.example.aiagenda.ui.ProfileFragment
import com.example.aiagenda.ui.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.checkerframework.common.subtyping.qual.Bottom

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.idNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.loginFragment || nd.id == R.id.signUpFragment || nd.id == R.id.forgotPasswordFragment
                || nd.id == R.id.dialogFragment
            ) {
                binding.bnvMenu.visibility = View.GONE
            } else {
                binding.bnvMenu.visibility = View.VISIBLE
            }
        }

        binding.bnvMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }
                R.id.menu -> {
                    replaceFragment(MenuFragment())
                }
                R.id.settings -> {
                    replaceFragment(SettingsFragment())
                }
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.idNavHostFragment,
            fragment
        )
        transaction.commit()
    }
}