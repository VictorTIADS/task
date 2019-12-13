package com.example.task.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.task.R
import com.example.task.adapter.TaskListAdapter
import com.example.task.constants.TaskConstants
import com.example.task.entities.TaskEntity
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.util.SecurityPreferences
import com.example.task.viewmodel.TaskListFragmentViewModel
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import org.koin.androidx.viewmodel.ext.android.viewModel


class TaskListFragment : Fragment(), View.OnClickListener {

    lateinit var mContext: Context
    private val viewModel: TaskListFragmentViewModel by viewModel()
    private lateinit var mRecycleTaskList: RecyclerView
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mAdapter: TaskListAdapter
    private lateinit var swipeIcon: Drawable
    private var swipeBackgraund = ColorDrawable(Color.TRANSPARENT)
    private var mTaskFilter = 0
    private lateinit var loader: View


    override fun onClick(view: View?) {
        when (view!!.id) {

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservable()

        if (arguments != null) {
            mTaskFilter = arguments!!.getInt(TaskConstants.TASK_FILTER.KEY)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        mContext = rootView.context
        loader = rootView.findViewById(R.id.my_loading_shimmer)
        mRecycleTaskList = rootView.findViewById(R.id.recycleView)
        return rootView
    }

    private fun configureAdapter(taskList: MutableList<TaskEntity>) {
        mAdapter = TaskListAdapter(mContext, taskList)
        mRecycleTaskList.adapter = mAdapter
        mRecycleTaskList.layoutManager = LinearLayoutManager(mContext)
        mAdapter.notifyDataSetChanged()
        swipeIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_delete_black_24dp)!!




        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onSwiped(taskViewHolder: RecyclerView.ViewHolder, direction: Int) {
                val box = alert("Deseja realmente deletar esta tarefa?", "Deletar Tarefa") {
                    yesButton {
                        viewModel.removeItemFromTheList(
                            taskList[taskViewHolder.adapterPosition].taskId,
                            taskList[taskViewHolder.adapterPosition].userId
                        )
                    }
                    noButton {
                        YoYo.with(Techniques.BounceInRight).onEnd {
                            loadTask()
                        }.duration(500).playOn(taskViewHolder.itemView)
                    }

                }
                box.isCancelable = false
                box.show()

            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemview = viewHolder.itemView
                val iconMargin = (itemview.height - swipeIcon.intrinsicHeight) / 2
                if (dX > 0) {
                    swipeBackgraund.setBounds(itemview.left, itemview.top, dX.toInt(), itemview.bottom)
                    swipeIcon.setBounds(itemview.left, itemview.top + iconMargin, itemview.left + swipeIcon.intrinsicWidth, itemview.bottom - iconMargin)

                }
                swipeBackgraund.draw(c)
                swipeIcon.draw(c)
                c.save()
                c.restore()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun onMoved(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                fromPos: Int,
                target: RecyclerView.ViewHolder,
                toPos: Int,
                x: Int,
                y: Int
            ) {

                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(mRecycleTaskList)

    }

    override fun onResume() {
        loadTask()
        super.onResume()
    }

    private fun setObservable() {
        viewModel.mTaskLists.observe(this, Observer {
            when (it.status) {
                BaseModel.Companion.STATUS.LOADING -> {
                    Log.i("aspk", "LOADING TASK LIST")
                }
                BaseModel.Companion.STATUS.SUCCESS -> {
                    if (it.message != "EMPTY_TASKS") {
                        it.data?.let { it1 -> configureAdapter(it1) }
                        controlViewLoader(BaseModel.Companion.STATUS.SUCCESS)
                        Log.i("aspk", "SUCCESSFUL TASK LIST")
                    } else {
                        Log.i("aspk", "SUCCESSFUL TASK LIST WARNING:EMPTY_TAKS")
                        fragmentManager?.beginTransaction()
                            ?.replace(R.id.frameAppBarMain, EmptyStateFragment.newInstance())
                            ?.commit()
                    }

                }
                BaseModel.Companion.STATUS.ERROR -> {
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.frameAppBarMain, ErroStateFragment.newInstance(it.message))
                        ?.commit()
                    Log.i("aspk", "ERROR TASKT LIST")
                }
            }
        })
        viewModel.stateTask.observe(this, Observer {
            when (it.status) {
                StateLog.Companion.STATE.LOADING -> {
                    Log.i("aspk", "LOADING TASK DELETE")
                }
                StateLog.Companion.STATE.SUCCESS -> {

//                    viewModel.getListOfTasks()
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.frameAppBarMain, newInstance(0))?.commit()
                    Log.i("aspk", "SUCCESS TASK DELETE")
                }
                StateLog.Companion.STATE.ERROR -> {
                    Log.i("aspk", "ERROR TASK DELETE")
                }
            }
        })
    }

    private fun controlViewLoader(state: BaseModel.Companion.STATUS) {
        when (state) {
            BaseModel.Companion.STATUS.LOADING -> {

            }
            BaseModel.Companion.STATUS.SUCCESS -> {
                mRecycleTaskList.visibility = View.VISIBLE
                loader.visibility = View.GONE
            }
            BaseModel.Companion.STATUS.ERROR -> {

            }
        }
    }

    fun loadTask() {
        viewModel.getListOfTasks()
    }

    companion object {

        @JvmStatic
        fun newInstance(taskFilter: Int): TaskListFragment {
            val args: Bundle = Bundle()
            args.putInt(TaskConstants.TASK_FILTER.KEY, taskFilter)
            val fragment = TaskListFragment()
            fragment.arguments = args
            return fragment
        }

    }
}
