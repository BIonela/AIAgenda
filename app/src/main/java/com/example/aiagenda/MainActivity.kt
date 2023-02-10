package com.example.aiagenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.aiagenda.databinding.ActivityMainBinding
import com.example.aiagenda.ui.HomeFragment
import com.example.aiagenda.ui.MenuFragment
import com.example.aiagenda.ui.ProfileFragment
import com.example.aiagenda.ui.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        showBottomNavigationMenu()
        bottomNavigationMenuNavigation()
    }

    private fun bottomNavigationMenuNavigation() {
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

    private fun showBottomNavigationMenu() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.idNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.loginFragment
                || nd.id == R.id.signUpFragment
                || nd.id == R.id.forgotPasswordFragment
                || nd.id == R.id.dialogFragment
            ) {
                binding.bnvMenu.visibility = View.GONE
            } else {
                binding.bnvMenu.visibility = View.VISIBLE
            }
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