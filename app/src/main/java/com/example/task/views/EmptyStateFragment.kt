package com.example.task.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

import com.example.task.R
import com.example.task.constants.TaskConstants
import kotlinx.android.synthetic.main.empty_tasks_state.*


class EmptyStateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.empty_tasks_state, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        empty_task_message.alpha = 0f

        YoYo.with(Techniques.BounceInRight).onEnd {
            empty_task_message.alpha = 1f
            YoYo.with(Techniques.BounceInLeft).duration(200).playOn(empty_task_message)
        }.duration(500).playOn(empty_task_icon)
        super.onViewCreated(view, savedInstanceState)
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
