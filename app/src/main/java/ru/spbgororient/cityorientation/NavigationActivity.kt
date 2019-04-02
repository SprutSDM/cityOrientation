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
    var needChangeFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer_layout.addDrawerListener(this)

        navigation_view.setNavigationItemSelectedListener(this)

        if (DataController.instance.questId == "Quest ID")
            fragment = ListOfQuestsFragment.instance
        else if (DataController.instance.step >= DataController.instance.listOfTasks.size)
            fragment = FinishFragment.instance
        else if (DataController.instance.getTask().img == "")
            fragment = QuestTextFragment.instance
        else
            fragment = QuestTextImgFragment.instance
        fragment.tag
        supportFragmentManager.beginTransaction().replace(R.id.content_frame,  fragment).commit()

        navigation_view.getHeaderView(0).findViewById<TextView>(R.id.text_name_team).text = DataController.instance.teamName
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
            R.id.nav_quest -> WaitingToStartFragment.instance
            else -> ListOfQuestsFragment.instance
        }
        needChangeFragment = true
        title = item.title

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDrawerStateChanged(state: Int) {
        if (needChangeFragment && state == DrawerLayout.STATE_IDLE) {
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()
            needChangeFragment = false
        }
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
