package com.example.task.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.YuvImage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.task.R
import com.example.task.entities.TaskEntity
import kotlinx.android.synthetic.main.item_list.view.*
import org.jetbrains.anko.backgroundColor

class TaskListAdapter( val context: Context,val list: MutableList<TaskEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val task = list[position]

        val data = holder.itemView.itemview_lbl_date
        val descricao = holder.itemView.itemview_lbl_description
        val title  = holder.itemView.itemview_lbl_title
        val priority = holder.itemView.item_view_priority

        data.text = task.dueDate
        descricao.text = task.description
        title.text = task.title

        when (task.priorityId){
            1 -> {
                priority.backgroundColor = ContextCompat.getColor(context,R.color.colorRed)
            }
            2 -> {
                priority.backgroundColor = ContextCompat.getColor(context,R.color.colorYellow)
            }
            3 -> {
                priority.backgroundColor = ContextCompat.getColor(context,R.color.colorGreen)
            }
            4 -> {
                priority.backgroundColor = ContextCompat.getColor(context,R.color.colorBlue)
            }
        }

        holder.itemView.setOnClickListener {
            YoYo.with(Techniques.Pulse)
                .duration(350)
                .playOn(it)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false)
        return ViewlHolder(view)
    }

    override fun getItemCount() = list.size

    fun removeItemAtPosition(viewHolder:RecyclerView.ViewHolder){
        list.removeAt(viewHolder.adapterPosition)
        notifyItemChanged(viewHolder.adapterPosition)
    }

}






class ViewlHolder(val itemView: View) : RecyclerView.ViewHolder(itemView)