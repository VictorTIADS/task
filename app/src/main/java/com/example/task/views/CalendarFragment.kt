package com.example.task.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.task.R


class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.task_calendar, container, false)
    }


    companion object {

        @JvmStatic
        fun newInstance(): CalendarFragment {
            val args: Bundle = Bundle()
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }

    }


}
