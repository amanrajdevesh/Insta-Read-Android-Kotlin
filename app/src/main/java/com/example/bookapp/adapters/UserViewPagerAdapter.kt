package com.example.bookapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserViewPagerAdapter(private val fragmentManager: FragmentManager, lifecycle: Lifecycle, private val list : ArrayList<Fragment>): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

}