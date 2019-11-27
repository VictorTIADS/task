package com.example.task.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.task.R
import com.example.task.constants.TaskConstants


class EmptyStateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.empty_tasks_state, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(): EmptyStateFragment {
            val args:Bundle = Bundle()
            val fragment = EmptyStateFragment()
            fragment.arguments = args
            return fragment
        }

    }


}
