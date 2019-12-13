package com.example.task.views

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.task.R
import com.example.task.adapter.PageAdapter
import com.example.task.business.PriorityBusiness
import com.example.task.constants.PriorityCacheConstants
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.viewmodel.MainViewModel
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_erro_state.*
import kotlinx.android.synthetic.main.fragment_erro_state.view.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.android.synthetic.main.header_state_error.*
import kotlinx.android.synthetic.main.header_state_error.view.*
import kotlinx.android.synthetic.main.hint_header.*
import kotlinx.android.synthetic.main.hint_header.view.*
import org.jetbrains.anko.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    private val viewModel: MainViewModel by viewModel()
    private lateinit var mHeaderInfo: View
    lateinit var mViewPager:ViewPager
    lateinit var mTabLayout:TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
//        mViewPager = pager
//        mTabLayout = tablelayout
//        lbl_content_main_user_name.text = viewModel.getNameCurrentUser()

//        loadPriorityCache()
        startDefaultFragment()
        setObservable()


//        setupViewPager(mViewPager)
//        mTabLayout.setupWithViewPager(mViewPager)




        floatAddTask.setOnClickListener {
            floatAddTask.isClickable = false
            YoYo.with(Techniques.RubberBand)
                .onEnd {
                    callTaskFormActivity()
                }
                .duration(500)
                .playOn(it)
        }
        mHeaderInfo.setOnClickListener {
            if (mHeaderInfo.profileHeaderImage.visibility == View.VISIBLE){
                startSomeFragment(UserInfoFragment.newInstance(viewModel.getNameCurrentUser(),viewModel.getEmailCurrentUser(),viewModel.getPhotoCurrentUser()))
                floatAddTask.hide()
                mDrawerLayout.closeDrawers()
            }else{
                val box = alert(
                    "Pedimos desculpas, não conseguimos carregar seus dados.\n\nPara Resolver, efetue novamente o login.",
                    "Ops!, Algo de Errado Aconteceu"
                ) {
                    okButton {
                        viewModel.clearSharedPreferences()
                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(baseContext, LoginActivity::class.java))
                        finish()
                    }

                }
                box.show()
            }


        }

        NavigationViewMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.MenuItemNews -> {

                    true
                }
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
                    floatAddTask.show()
                    floatAddTask.isClickable = true
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

//    private fun setupViewPager(viewPager: ViewPager){
//        val adapter = PageAdapter(supportFragmentManager)
//        adapter.addFragment(CalendarFragment.newInstance(),"Inicio")
//        adapter.addFragment(TaskListFragment.newInstance(TaskConstants.TASK_FILTER.TODO),"Tarefas")
//        viewPager.adapter = adapter
//    }

    override fun onResume() {
        floatAddTask.isClickable = true
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
                mHeaderInfo.my_hind_shimmer_header.visibility = View.GONE
                mHeaderInfo.header_state_error.visibility = View.VISIBLE
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
        floatAddTask.hide()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mToggle.onOptionsItemSelected(item)) {
            mToggle.syncState()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNameAndEmailInTheHeaderMenu() {
        mHeaderInfo.lblNameHeaderMenu.text = viewModel.getNameCurrentUser()
        mHeaderInfo.lblEmailHeaderMenu.text = viewModel.getEmailCurrentUser()
    }

    private fun setImageProfileWithPicasso() {
        if (viewModel.getPhotoCurrentUser()!!.isEmpty() || viewModel.getPhotoCurrentUser()!!.isBlank()) {
            val box = alert(
                "Pedimos desculpas, não conseguimos carregar seus dados.\n\nPara Resolver, efetue novamente o login.",
                "Ops!, Algo de Errado Aconteceu"
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
                .into(mHeaderInfo.profileHeaderImage)
        }

    }

}
