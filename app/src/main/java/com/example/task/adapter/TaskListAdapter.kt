package com.example.task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.constants.PriorityCacheConstants
import com.example.task.entities.TaskEntity
import kotlinx.android.synthetic.main.item_list.view.*

class TaskListAdapter( val context: Context,val list: MutableList<TaskEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = list[position]

        val data = holder.itemView.lblDataList
        val descricao = holder.itemView.lblDescricaoList
        val prioridade  = holder.itemView.lblPrioridadeList
        val status = holder.itemView.lblStatusList

        data.text = task.dueDate
        descricao.text = task.description
        prioridade.text = PriorityCacheConstants.getPriorityDescription(task.priorityId)
        status.text = if (task.complete) "Complete" else "Pendente"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false)
        return ViewlHolder(view)
    }

    override fun getItemCount() = list.size



}






class ViewlHolder(val itemView: View) : RecyclerView.ViewHolder(itemView)