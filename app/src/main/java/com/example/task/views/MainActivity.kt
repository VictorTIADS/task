package com.example.task.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.task.R
import com.example.task.business.PriorityBusiness
import com.example.task.constants.PriorityCacheConstants
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.android.synthetic.main.header_state_error.*
import kotlinx.android.synthetic.main.hint_header.*
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var viewModel: MainViewModel
    private lateinit var mHeaderInfo: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.initSharedPreferences(this)
        viewModel.getUserData()


        mToolbar = my_toolbar
        setSupportActionBar(mToolbar)
        mPriorityBusiness = PriorityBusiness(this)
        mDrawerLayout = findViewById(R.id.drawer)
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mHeaderInfo = NavigationViewMain.getHeaderView(0)
//        lbl_content_main_user_name.text = viewModel.getNameCurrentUser()


        loadPriorityCache()
        startDefaultFragment()
        setObservable()




        floatAddTask.setOnClickListener {
            callTaskFormActivity()
        }

        NavigationViewMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.MenuItemSair -> {
                    val box = alert("Você está prestes a sair.", "Deseja realmente sair?") {
                        yesButton {
                            viewModel.signOutUser()
                        }
                        noButton {}
                    }
                    box.show()
                    true
                }
                R.id.MenuItemInicio -> {
                    startDefaultFragment()
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.MenuItemSobre -> {
                    startSomeFragment(AboutFragment.newInstance())
                    mDrawerLayout.closeDrawers()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun onResume() {
        startDefaultFragment()
        super.onResume()
    }

    private fun callTaskFormActivity() {
        val intentTaskForm = Intent(this, TaskFormActivity::class.java)
        intentTaskForm.putExtra(TaskConstants.KEY.USER_ID, viewModel.getIdCurrentUser())
        startActivity(intentTaskForm)
    }


    private fun setObservable() {
        viewModel.user.observe(this, Observer {
            when (it.status) {
                BaseModel.Companion.STATUS.LOADING -> {
                    controlVisibleHeaderInfo(BaseModel.Companion.STATUS.LOADING)
                    Log.i("aspk", "LOADING DOCUMENT USER")
                }
                BaseModel.Companion.STATUS.SUCCESS -> {
                    controlVisibleHeaderInfo(BaseModel.Companion.STATUS.SUCCESS)
                    Log.i("aspk", "SUCCESSFUL DOCUMENT USER")
                }
                BaseModel.Companion.STATUS.ERROR -> {
                    controlVisibleHeaderInfo(BaseModel.Companion.STATUS.ERROR)
                    Log.i("aspk", "ERROR DOCUMENT USER")
                }

            }
        })
        viewModel.userIsLoged.observe(this, Observer {
            when (it.status) {
                StateLog.Companion.STATE.FALSE -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        })
    }

    private fun controlVisibleHeaderInfo(state: BaseModel.Companion.STATUS) {
        when (state) {
            BaseModel.Companion.STATUS.SUCCESS -> {
                setNameAndEmailInTheHeaderMenu()
                setImageProfileWithPicasso()
                my_hind_shimmer_header.visibility = View.GONE
                profileHeaderImage.visibility = View.VISIBLE
                lblEmailHeaderMenu.visibility = View.VISIBLE
                lblNameHeaderMenu.visibility = View.VISIBLE


            }
            BaseModel.Companion.STATUS.ERROR -> {
                my_hind_shimmer_header.visibility = View.GONE
                header_state_error.visibility = View.VISIBLE
            }
        }
    }

    private fun loadPriorityCache() {
        PriorityCacheConstants.setCache(mPriorityBusiness.getList())
    }

    private fun startDefaultFragment() {
        var fragment: Fragment = TaskListFragment.newInstance(TaskConstants.TASK_FILTER.TODO)
        supportFragmentManager.beginTransaction().replace(R.id.frameAppBarMain, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
    }

    fun startSomeFragment(instance: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameAppBarMain, instance)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mToggle.onOptionsItemSelected(item)) {
            mToggle.syncState()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNameAndEmailInTheHeaderMenu() {
        lblNameHeaderMenu.text = viewModel.getNameCurrentUser()
        lblEmailHeaderMenu.text = viewModel.getEmailCurrentUser()
    }

    private fun setImageProfileWithPicasso() {
        if (viewModel.getPhotoCurrentUser()!!.isEmpty() || viewModel.getPhotoCurrentUser()!!.isBlank()) {
            val box = alert(
                "Pedimos desculpas, não conseguimos carregar seus dados. Efetue novamente o login.",
                "Sessão Expirada"
            ) {
                okButton {

                    viewModel.clearSharedPreferences()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                    finish()
                }

            }
            box.isCancelable = false
            box.show()
        } else {
            Picasso.get().load(viewModel.getPhotoCurrentUser())
                .placeholder(R.drawable.user_default).centerCrop().resize(500, 500)
                .into(profileHeaderImage)
        }

    }

}
