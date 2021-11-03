package com.example.bookapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.bookapp.R
import com.example.bookapp.databinding.ActivityMainBinding
import com.example.bookapp.viewModels.BookViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
        }
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val navigationBottom = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        drawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(this , navController , drawerLayout)
        NavigationUI.setupWithNavController(binding.navView , navController)


        navigationBottom.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                // do something here
                Log.d("sonusourav","is null ${FirebaseAuth.getInstance().currentUser == null}")
                startActivity(Intent(this , LoginActivity::class.java))
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController , drawerLayout)
    }

}