package ru.spbgororient.cityorientation.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_navigation.*
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.fragments.listOfQuests.ListOfQuestsFragment
import ru.spbgororient.cityorientation.fragments.myTeam.MyTeamFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextImgFragment
import ru.spbgororient.cityorientation.fragments.waitingToStart.WaitingToStartFragment
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.fragments.finish.NoQuestSelectedFragment
import ru.spbgororient.cityorientation.network.Network
import kotlin.concurrent.timer


class NavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        navigation_view.setOnNavigationItemSelectedListener(this)

        navigation_view.selectedItemId = when {
            DataController.instance.quests.questId == "" -> R.id.nav_list_of_quests
            else -> R.id.nav_quest
        }

        // startBackgroundUpdate()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("NavigationActivity", "item selected: ${item.title}")
        fragment = when (item.itemId) {
            R.id.nav_my_group -> MyTeamFragment.instance
            R.id.nav_list_of_quests -> ListOfQuestsFragment.instance
            R.id.nav_quest -> {
                val quest = DataController.instance.quests.getQuest()
                DataController.instance.quests.let {
                    when {
                        quest == null -> NoQuestSelectedFragment.instance
                        quest.startTime * 1000 > DataController.instance.currentTime  -> WaitingToStartFragment.instance
                        it.isFinished || (quest.startTime + quest.duration) * 1000 <= DataController.instance.currentTime -> FinishFragment.instance
                        it.getTask().img == "" -> QuestTextFragment.instance
                        it.getTask().img != "" -> QuestTextImgFragment.instance
                        else -> WaitingToStartFragment.instance
                    }
                }
            }
            else -> ListOfQuestsFragment.instance
        }
        loadFragment(fragment)
        title = item.title
        return true
    }
}
