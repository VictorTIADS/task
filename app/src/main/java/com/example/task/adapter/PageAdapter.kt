package com.example.task.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.task.views.EmptyStateFragment
import com.example.task.views.TaskListFragment

class PageAdapter(fm: FragmentManager):FragmentPagerAdapter(fm){

    private var mFragmentList:ArrayList<Fragment> = arrayListOf()
    private var mFragmentTitleList:ArrayList<String> = arrayListOf()


    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    fun addFragment(mFragment: Fragment,title:String){
        mFragmentList.add(mFragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): String = mFragmentTitleList[position]


}