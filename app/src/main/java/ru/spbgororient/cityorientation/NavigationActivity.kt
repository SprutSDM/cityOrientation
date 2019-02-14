package ru.spbgororient.cityorientation

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.fragments.listOfQuests.ListOfQuestsFragment
import ru.spbgororient.cityorientation.fragments.myTeam.MyTeamFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextImgFragment
import ru.spbgororient.cityorientation.fragments.waitingToStart.WaitingToStartFragment
import ru.spbgororient.cityorientation.questsController.DataController
import android.view.inputmethod.InputMethodManager


class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DrawerLayout.DrawerListener {
    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer_layout.addDrawerListener(this)

        nav_view.setNavigationItemSelectedListener(this)

        if (DataController.instance.questId == "Quest ID")
            fragment = ListOfQuestsFragment.instance
        else if (DataController.instance.step >= DataController.instance.listOfTasks.size)
            fragment = FinishFragment.instance
        else if (DataController.instance.getTask().img == "")
            fragment = QuestTextFragment.newInstance()
        else
            fragment = QuestTextImgFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.content_frame,  fragment).commit()

        nav_view.getHeaderView(0).findViewById<TextView>(R.id.labelTeamName).text = DataController.instance.teamName
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        lateinit var fragment: Fragment

        when (item.itemId) {
            R.id.nav_my_group -> {
                fragment = MyTeamFragment.instance
            }
            R.id.nav_list_of_quests -> {
                fragment = ListOfQuestsFragment.instance
            }
            R.id.nav_quest -> {
                fragment = WaitingToStartFragment.instance
            }
        }
        title = item.title
        supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*
    findViewById<EditText>(R.id.search).setOnEditorActionListener { v, actionId, event ->
    return@setOnEditorActionListener when (actionId) {
        EditorInfo.IME_ACTION_SEND -> {
            sendMessage()
            true
        }
        else -> false
    }
     */
    override fun onDrawerStateChanged(p0: Int) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        currentFocus!!.clearFocus()
    }

    override fun onDrawerSlide(p0: View, p1: Float) {
    }

    override fun onDrawerClosed(p0: View) {
    }

    override fun onDrawerOpened(p0: View) {
    }
}
