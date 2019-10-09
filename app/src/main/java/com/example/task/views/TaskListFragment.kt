package com.example.task.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.adapter.TaskListAdapter
import com.example.task.business.TaskBusiness
import com.example.task.constants.TaskConstants
import com.example.task.entities.TaskEntity
import com.example.task.util.SecurityPreferences
import com.google.android.material.floatingactionbutton.FloatingActionButton

lateinit var mContext: Context
private lateinit var mRecycleTaskList:RecyclerView
private lateinit var mTaskBusiness: TaskBusiness
private lateinit var mSecurityPreferences: SecurityPreferences
private var mTaskFilter = 0


class TaskListFragment : Fragment(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.floatAddTask -> {
                startActivity(Intent(mContext, TaskFormActivity::class.java))

            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            mTaskFilter = arguments!!.getInt(TaskConstants.TASK_FILTER.KEY)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        rootView.findViewById<FloatingActionButton>(R.id.floatAddTask).setOnClickListener(this)
        mContext = rootView.context

        mTaskBusiness = TaskBusiness(mContext)
        mSecurityPreferences = SecurityPreferences(mContext)

        mRecycleTaskList = rootView.findViewById(R.id.recycleView)

        val userId = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)!!.toInt()
        val taskList = mTaskBusiness.getList(userId,taskFilter = 0)


        mRecycleTaskList.adapter = TaskListAdapter(mContext,taskList)


        mRecycleTaskList.adapter = TaskListAdapter(mContext, mutableListOf())
        mRecycleTaskList.layoutManager = LinearLayoutManager(mContext)




        return rootView
    }

    override fun onResume() {
        loadTask()
        super.onResume()
    }

    fun loadTask(){
        val userId = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)!!.toInt()
        val taskList = mTaskBusiness.getList(userId, mTaskFilter)


        mRecycleTaskList.adapter = TaskListAdapter(mContext,taskList)
        (mRecycleTaskList.adapter as TaskListAdapter).notifyDataSetChanged()
    }

    companion object {

        @JvmStatic
        fun newInstance(taskFilter:Int): TaskListFragment {
            val args:Bundle = Bundle()
            args.putInt(TaskConstants.TASK_FILTER.KEY,taskFilter)
            val fragment = TaskListFragment()
            fragment.arguments = args
            return fragment
        }

    }
}
