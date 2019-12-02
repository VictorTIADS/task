package com.example.task.views


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

import com.example.task.R
import com.example.task.constants.TaskConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_info_task.*


class UserInfoFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadInfo()
        super.onViewCreated(view, savedInstanceState)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_info_task, container, false)



    }
    private fun loadInfo(){
        Picasso.get().load(arguments?.getString(TaskConstants.KEY.USER_PROFILE))
            .placeholder(R.drawable.user_no_picture).centerCrop().resize(500, 500)
            .into(photo_about_user)
        lbl_info_user_name.text = arguments?.getString(TaskConstants.KEY.USER_NAME)
        lbl_info_user_email.text = arguments?.getString(TaskConstants.KEY.USER_EMAIL)

    }




    companion object {

        @JvmStatic
        fun newInstance(nome:String?,email:String?,foto:String?): UserInfoFragment {
            val args: Bundle = Bundle()
            args.putString(TaskConstants.KEY.USER_NAME,nome)
            args.putString(TaskConstants.KEY.USER_EMAIL,email)
            args.putString(TaskConstants.KEY.USER_PROFILE,foto)
            val fragment = UserInfoFragment()
            fragment.arguments = args
            return fragment
        }

    }


}
