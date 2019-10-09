package com.example.task.views

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.task.R
import com.example.task.business.PriorityBusiness
import com.example.task.business.TaskBusiness
import com.example.task.constants.TaskConstants
import com.example.task.entities.PriorityEntity
import com.example.task.entities.TaskEntity
import com.example.task.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_task_form.*
import org.jetbrains.anko.toast
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener{

    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences

    private val mSimpleDateFormat:SimpleDateFormat = SimpleDateFormat("dd/MM/yy")


    private var mLstPriorityEntity: MutableList<PriorityEntity> = mutableListOf()
    private var mLstPriorityId: MutableList<Int> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        //INSTANCIA
        mTaskBusiness = TaskBusiness(this)
        mPriorityBusiness = PriorityBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)

        //LOADS
        loadPriorities()

        btnAdicionar.setOnClickListener {
            handleSave()
        }

        txtData.setOnClickListener {
            opendDatePickerDialog()
        }
    }

    private fun loadPriorities(){
        mLstPriorityEntity = mPriorityBusiness.getList()
        val lstPriorities = mLstPriorityEntity.map { it.description }
         mLstPriorityId = mLstPriorityEntity.map { it.id }.toMutableList()
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lstPriorities)
        spinnerPriority.adapter = adapter

    }

    private fun opendDatePickerDialog(){
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var  day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this,this,year,month,day).show()
    }
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day_m: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day_m)
        txtData.setText(mSimpleDateFormat.format(calendar.time))
    }

    private fun handleSave(){
        try {
            val description = txtDescricao.text.toString()
            val priorityId = mLstPriorityId[spinnerPriority.selectedItemPosition]
            val complete = checkCompleto.isChecked
            val dueDate = txtData.text.toString()
            val userId = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)?.toInt()
            val taskEntity = userId?.let {
                TaskEntity(0,
                    it,priorityId,description,dueDate,complete)
            }
            taskEntity?.let { mTaskBusiness.insert(it) }
            finish()

        }catch (e:Exception){
            toast("Erro")
        }
    }

}
