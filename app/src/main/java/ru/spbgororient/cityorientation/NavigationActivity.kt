package ru.spbgororient.cityorientation

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
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

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        lateinit var fragment: Fragment
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

        supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
