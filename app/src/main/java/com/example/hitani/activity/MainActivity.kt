package com.example.hitani.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hitani.R
import com.example.hitani.databinding.ActivityMainBinding
import com.example.hitani.preferences.UserPreferences
import com.example.hitani.view.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var preferences: UserPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        preferences = UserPreferences(this)

        window.navigationBarColor = this.resources.getColor(android.R.color.black)
        setupActionBarWithNavController(findNavController(R.id.nav_host))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                preferences.logOut()
                moveIntentLogOut()
                finish()
                true
            }
            else -> true
        }
    }

    private fun moveIntentLogOut() {
        Intent(this, LoginActivity::class.java).also{
            startActivity(it)
            finish()
        }
    }
}