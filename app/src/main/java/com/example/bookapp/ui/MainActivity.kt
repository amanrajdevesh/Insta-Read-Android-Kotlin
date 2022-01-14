package com.example.bookapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.example.bookapp.R
import com.example.bookapp.databinding.ActivityMainBinding
import com.example.bookapp.network.BookService
import com.example.bookapp.ui.fragments.*
import com.example.bookapp.viewModels.BookViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    //lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    @Inject lateinit var auth : FirebaseAuth
    private lateinit var feedFragment : FeedFragment
    private lateinit var searchFragment : BookSearchFragment
    private lateinit var postBookFragment : PostBookFragment
    private lateinit var userFragment : UserFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
        }
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        drawerLayout = binding.drawerLayout

        feedFragment = FeedFragment()
        searchFragment = BookSearchFragment()
        postBookFragment = PostBookFragment()
        userFragment = UserFragment()

        setCurrentFragment(searchFragment)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.feed -> setCurrentFragment(feedFragment)
                R.id.search -> setCurrentFragment(searchFragment)
                R.id.post -> setCurrentFragment(postBookFragment)
                R.id.user -> setCurrentFragment(userFragment)
            }
            true
        }

        setUpDrawerContent(binding.navView)

        updateNavDrawer()

    }

    private fun setUpDrawerContent(navView: NavigationView) {
        navView.setNavigationItemSelectedListener { item ->
            selectDrawerItem(item)
            true
        }
    }

    private fun selectDrawerItem(item: MenuItem) {
        when(item.itemId){
            R.id.feedFrag -> setCurrentFragment(feedFragment)
            R.id.searchFrag -> setCurrentFragment(searchFragment)
            R.id.postBookFrag -> setCurrentFragment(postBookFragment)
            R.id.userFrag -> setCurrentFragment(userFragment)
        }
        //item.isChecked = true
        title = item.title
        drawerLayout.closeDrawers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                AuthUI.getInstance().signOut(this).addOnCompleteListener {
                    // do something here
                    Log.d("sonusourav","is null ${FirebaseAuth.getInstance().currentUser == null}")
                    startActivity(Intent(this , LoginActivity::class.java))
                    finish()
            }
        }
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    /*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController , drawerLayout)
    }

     */

    private fun updateNavDrawer() {
        val currentUser = auth.currentUser
        val headerView : View = binding.navView.getHeaderView(0)
        val headerName = headerView.findViewById<TextView>(R.id.navHeaderName)
        val headerId = headerView.findViewById<TextView>(R.id.navHeaderId)
        val headerImage = headerView.findViewById<ImageView>(R.id.navHeaderImage)

        headerName.text = currentUser?.displayName
        headerId.text = currentUser?.email
        Picasso.get().load(currentUser?.photoUrl).into(headerImage)

    }

    private fun setCurrentFragment(fragment : Fragment){
        val backStateName: String = fragment.javaClass.name
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
        if(!fragmentPopped){
            manager.beginTransaction().apply {
                replace(R.id.container,fragment,"InstaRead")
                addToBackStack(backStateName)
                commit()
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        }else if(binding.bottomNavigationView.selectedItemId != R.id.feed)
        {
            binding.bottomNavigationView.selectedItemId = R.id.feed;
        }
        else {
            super.onBackPressed()
        }
    }

}