package com.ajiswn.dicodingevent.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ajiswn.dicodingevent.R
import com.ajiswn.dicodingevent.databinding.ActivityHomeBinding
import com.ajiswn.dicodingevent.ui.search.SearchActivity
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_upcoming,
                R.id.navigation_finished,
                R.id.navigation_favorite,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavIcons(destination.id)
        }
    }

    private fun updateBottomNavIcons(activeId: Int) {
        val navView: BottomNavigationView = binding.navView

        for (i in 0 until navView.menu.size()) {
            val menuItem = navView.menu.getItem(i)
            when (menuItem.itemId) {
                R.id.navigation_home -> menuItem.setIcon(
                    if (menuItem.itemId == activeId) R.drawable.ic_home_fill_24dp else R.drawable.ic_home_outline_24dp
                )
                R.id.navigation_upcoming -> menuItem.setIcon(
                    if (menuItem.itemId == activeId) R.drawable.ic_upcoming_fill_24dp else R.drawable.ic_upcoming_outline_24dp
                )
                R.id.navigation_finished -> menuItem.setIcon(
                    if (menuItem.itemId == activeId) R.drawable.ic_finished_fill_24dp else R.drawable.ic_finished_outline_24dp
                )
                R.id.navigation_favorite -> menuItem.setIcon(
                    if (menuItem.itemId == activeId) R.drawable.ic_favorite_fill_24dp else R.drawable.ic_favorite_outline_24dp
                )
                R.id.navigation_setting -> menuItem.setIcon(
                    if (menuItem.itemId == activeId) R.drawable.ic_setting_fill_24dp else R.drawable.ic_setting_outline_24dp
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}