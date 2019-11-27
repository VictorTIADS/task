package com.example.task.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.task.R


class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.about_about, container, false)
    }
    companion object {

        @JvmStatic
        fun newInstance(): AboutFragment {
            val args: Bundle = Bundle()
            val fragment = AboutFragment()
            fragment.arguments = args
            return fragment
        }

    }


}
