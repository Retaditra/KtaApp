package com.kta.app.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kta.app.profile.ProfileFragment
import com.kta.app.kta.KtaFragment
import com.kta.app.schedule.ScheduleFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> KtaFragment()
            1 -> ScheduleFragment()
            2 -> ProfileFragment()
            else -> KtaFragment()
        }
    }
}
