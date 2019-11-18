package com.example.task.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.task.R
import com.example.task.constants.TaskConstants

private lateinit var viewModel: TaskFormViewModel

class TaskFormFragment : Fragment(),View.OnClickListener {
    override fun onClick(p0: View?) {

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.task_form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TaskFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance() = TaskFormFragment()
    }


}
