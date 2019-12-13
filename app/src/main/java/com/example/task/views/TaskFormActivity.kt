package com.example.task.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.task.R
import com.example.task.animation.radioFadeIn
import com.example.task.animation.radioFadeOut
import com.example.task.business.PriorityBusiness
import com.example.task.constants.TaskConstants
import com.example.task.entities.PriorityEntity
import com.example.task.entities.TaskEntity
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.util.SecurityPreferences
import com.example.task.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_task_form.*
import kotlinx.android.synthetic.main.item_form_date.*
import kotlinx.android.synthetic.main.item_form_description.*
import kotlinx.android.synthetic.main.item_form_rdpriority.*
import kotlinx.android.synthetic.main.item_form_title.*
import kotlinx.android.synthetic.main.toolbar_without_corners.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.design.longSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences
    private val mSimpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yy")
    private val viewModel: TaskFormViewModel by viewModel()
    private var priorityRadiobutton = 0
    private var mDate = ""



    private var mLstPriorityEntity: MutableList<PriorityEntity> = mutableListOf()
    private var mLstPriorityId: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)


        //INSTANCIA
        mPriorityBusiness = PriorityBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)
        //LOADS
//        loadPriorities()
        configuretoolbar()
        setObservable()

//        btnAdicionar.setOnClickListener {
////            handleSave()
//        }
//
//        txtData.setOnClickListener {
////            opendDatePickerDialog()
//        }

        icon_form_calendar.setOnClickListener {
            opendDatePickerDialog()
        }
        card_date.setOnClickListener {
            opendDatePickerDialog()
        }
        icon_from_edit.setOnClickListener {
            opendDatePickerDialog()
        }

        listOf(radio_form_1, radio_form_2, radio_form_3, radio_form_4).forEach {
            it.setOnClickListener { view ->
                controlColorPriority(view)
            }
        }
        btn_form_add_task.setOnClickListener {
            handleadd()
        }


    }

    private fun setObservable() {
        viewModel.mTask.observe(this, androidx.lifecycle.Observer {
           when(it.status){
               BaseModel.Companion.STATUS.LOADING ->{
                   controlViewLoader(BaseModel.Companion.STATUS.LOADING)

               }
               BaseModel.Companion.STATUS.SUCCESS ->{
                   controlViewLoader(BaseModel.Companion.STATUS.SUCCESS)
               }
               BaseModel.Companion.STATUS.ERROR ->{
                   controlViewLoader(BaseModel.Companion.STATUS.ERROR)

               }
           }
        })
    }

    private fun controlViewLoader(state: BaseModel.Companion.STATUS) {
        when (state) {
            BaseModel.Companion.STATUS.LOADING -> {
                btn_form_add_task.text = null
                btn_form_add_task.isEnabled = false
                load_form_spin.visibility = View.VISIBLE
            }
            BaseModel.Companion.STATUS.SUCCESS -> {
                //criar uma view para sucesso
                onBackPressed()
            }
            BaseModel.Companion.STATUS.ERROR -> {
                //criar uma view para erro
                btn_form_add_task.isEnabled = true
                load_form_spin.visibility = View.GONE
                btn_form_add_task.longSnackbar("Erro inesperado")


            }
        }
    }


    private fun handleadd() {
        var userId = intent.getStringExtra(TaskConstants.KEY.USER_ID)
        val title = txt_form_title.text.toString()
        val description = txt_form_description.text.toString()
        val idPriority = priorityRadiobutton
        val date = mDate
        val task = TaskEntity("" ,userId!!, idPriority, title, description, date, false)
        viewModel.addNewTask(task)

    }

    fun changeColor(color: Int) {
        form_priority_color_priority.radioFadeOut {
            form_priority_color_priority.backgroundColor = color
            form_priority_color_priority.radioFadeIn { }
        }

        form_priority_color_date.radioFadeOut {
            form_priority_color_date.backgroundColor = color
            form_priority_color_date.radioFadeIn { }
        }

        form_priority_color_description.radioFadeOut {
            form_priority_color_description.backgroundColor = color
            form_priority_color_description.radioFadeIn { }
        }

        form_priority_color_titulo.radioFadeOut {
            form_priority_color_titulo.backgroundColor = color
            form_priority_color_titulo.radioFadeIn { }
        }
    }

    private fun controlColorPriority(state: View) {
        when (state) {
            radio_form_1 -> {
                changeColor(ContextCompat.getColor(this,R.color.colorRed))
                priorityRadiobutton = 1
            }
            radio_form_2 -> {
                changeColor(ContextCompat.getColor(this,R.color.colorYellow))
                priorityRadiobutton = 2
            }
            radio_form_3 -> {
                changeColor(ContextCompat.getColor(this,R.color.colorGreen))
                priorityRadiobutton = 3
            }
            radio_form_4 -> {
                changeColor(ContextCompat.getColor(this,R.color.colorBlue))
                priorityRadiobutton = 4
            }
        }
    }

    private fun configuretoolbar() {
        setSupportActionBar(toolbar_withoutcorner)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.subtitle = "Nova Tarefa"
    }

    private fun controlViewDate(state: StateLog.Companion.STATE) {
        when (state) {
            StateLog.Companion.STATE.SUCCESS -> {
                icon_form_calendar.visibility = View.GONE
                lbl_content_date_from_calendar.visibility = View.VISIBLE
                icon_from_edit.visibility = View.VISIBLE
            }
        }
    }

//    private fun loadPriorities(){
//        mLstPriorityEntity = mPriorityBusiness.getList()
//        val lstPriorities = mLstPriorityEntity.map { it.description }
//         mLstPriorityId = mLstPriorityEntity.map { it.id }.toMutableList()
//        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lstPriorities)
//        spinnerPriority.adapter = adapter
//
//    }

    private fun opendDatePickerDialog() {
        val c = Calendar.getInstance()

        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        val datepicker = DatePickerDialog(this, this, year, month, day)
        datepicker.datePicker.minDate = c.time.time
        datepicker.show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day_m: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day_m)
        lbl_content_date_from_calendar.text = mSimpleDateFormat.format(calendar.time)
        mDate = mSimpleDateFormat.format(calendar.time)
        controlViewDate(StateLog.Companion.STATE.SUCCESS)
    }

//    private fun handleSave(){
//        try {
//            val description = txtDescricao.text.toString()
//            val priorityId = mLstPriorityId[spinnerPriority.selectedItemPosition]
//            val complete = checkCompleto.isChecked
//            val dueDate = txtData.text.toString()
//            val userId = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)?.toInt()
//            val taskEntity = userId?.let {
//                TaskEntity(0,
//                    it,priorityId,description,dueDate,complete)
//            }
//            taskEntity?.let { mTaskBusiness.insert(it) }
//            finish()
//
//        }catch (e:Exception){
//            toast("Erro")
//        }
//    }

}
