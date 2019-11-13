package com.example.task.views

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.task.R
import com.example.task.business.PriorityBusiness
import com.example.task.constants.PriorityCacheConstants
import com.example.task.constants.TaskConstants
import com.example.task.model.MyUser
import com.example.task.util.SecurityPreferences
import com.example.task.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Cache
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_resister_acticity.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton


class MainActivity : AppCompatActivity() {
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.initSharedPreferences(this)
        mPriorityBusiness = PriorityBusiness(this)
        mDrawerLayout = findViewById(R.id.drawer)
        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadPriorityCache()
        startDefaultFragment()
        NavigationViewMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.MenuItemSair -> {
                    val box = alert("Você está prestes a sair.", "Deseja realmente sair?") {
                        yesButton {
                            viewModel.clearSharedPreferences()
                            FirebaseAuth.getInstance().signOut()
                            startActivity(Intent(baseContext, LoginActivity::class.java))
                            finish()
                        }
                        noButton {}
                    }
                    box.show()
                    true
                }
                R.id.MenuItemTarefasFeitas -> {
                    TaskListFragment.newInstance(TaskConstants.TASK_FILTER.COMPLETE)
                    toast("as")

                    true
                }
                R.id.MenuItemTarefasPendentes -> {
                    TaskListFragment.newInstance(TaskConstants.TASK_FILTER.TODO)
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun loadPriorityCache() {
        PriorityCacheConstants.setCache(mPriorityBusiness.getList())
    }

    private fun startDefaultFragment() {
        var fragment: Fragment = TaskListFragment.newInstance(TaskConstants.TASK_FILTER.TODO)
        supportFragmentManager.beginTransaction().replace(R.id.frameAppBarMain, fragment).commit()
    }

    fun startSomeFragment(instance: TaskListFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameAppBarMain, instance).commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setNameAndEmailInTheHeaderMenu()
        setImageProfileWithPicasso()
        if (mToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setNameAndEmailInTheHeaderMenu() {
        lblNameHeaderMenu.text = viewModel.getNameCurrentUser()
        lblEmailHeaderMenu.text = viewModel.getEmailCurrentUser()
    }

    private fun setImageProfileWithPicasso() {
        Picasso.get().load(viewModel.getPhotoCurrentUser())
            .placeholder(R.drawable.user_default).centerCrop().resize(500, 500).into(profileImage)
    }

}
