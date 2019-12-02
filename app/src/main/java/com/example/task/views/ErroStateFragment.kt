package com.example.task.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.task.R
import com.example.task.constants.TaskConstants
import kotlinx.android.synthetic.main.fragment_erro_state.*

/**
 * A simple [Fragment] subclass.
 */
class ErroStateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        error_task_message.text = arguments?.getString(TaskConstants.STATE.ERROR)
        return inflater.inflate(R.layout.fragment_erro_state, container, false)
    }




    companion object {

        @JvmStatic
        fun newInstance(errorMessage:String?): ErroStateFragment {
            val args:Bundle = Bundle()
            val fragment = ErroStateFragment()
            args.putString(TaskConstants.STATE.ERROR,errorMessage)
            fragment.arguments = args
            return fragment
        }

    }


}
