package ru.spbgororient.cityorientation.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
import android.view.inputmethod.InputMethodManager
import ru.spbgororient.cityorientation.App
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController


class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DrawerLayout.DrawerListener {
    lateinit var fragment: Fragment
    private var needChangeFragment = false

    private lateinit var teamName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer_layout.addDrawerListener(this)

        navigation_view.setNavigationItemSelectedListener(this)

        if (DataController.instance.quests.questId == "")
            fragment = ListOfQuestsFragment.instance
        else
            if (DataController.instance.quests.step >= DataController.instance.quests.listOfTasks.size)
                fragment = FinishFragment.instance
            else
                if (DataController.instance.quests.getTask().img == "")
                    fragment = QuestTextFragment.instance
                else
                    fragment = QuestTextImgFragment.instance
        fragment.tag
        supportFragmentManager.beginTransaction().replace(R.id.content_frame,  fragment).commit()

        teamName = navigation_view.getHeaderView(0).findViewById(R.id.text_name_team)
        teamName.text = DataController.instance.team.teamName
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        fragment = when (item.itemId) {
            R.id.nav_my_group -> MyTeamFragment.instance
            R.id.nav_list_of_quests -> ListOfQuestsFragment.instance
            R.id.nav_quest -> {
                if (DataController.instance.quests.isStarted)
                    if (DataController.instance.quests.getTask().img == "")
                        QuestTextFragment.instance
                    else
                        QuestTextImgFragment.instance
                else
                    WaitingToStartFragment.instance
            }
            else -> ListOfQuestsFragment.instance
        }
        needChangeFragment = true
        title = item.title

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDrawerStateChanged(state: Int) {
        if (state == DrawerLayout.STATE_IDLE) {
            // Замена фрагмента
            if (needChangeFragment) {
                supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()
                needChangeFragment = false
            }
            teamName.text = DataController.instance.team.teamName
        }
        // Скрытие клавиатуры
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

    private fun renameTeamCallback() {
        runOnUiThread {
            navigation_view.getHeaderView(0).findViewById<TextView>(R.id.text_name_team).text =
                DataController.instance.team.teamName
        }
    }
}
