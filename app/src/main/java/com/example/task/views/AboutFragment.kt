package com.example.task.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

import com.example.task.R
import kotlinx.android.synthetic.main.about_about.*
import kotlinx.android.synthetic.main.about_about.view.*


class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout =  inflater.inflate(R.layout.about_about, container, false)
        YoYo.with(Techniques.BounceIn)
            .duration(2000)
            .playOn(layout.my_layout_about)
        return layout


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
