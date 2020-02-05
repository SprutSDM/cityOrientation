package ru.spbgororient.cityorientation.activities.mainActivity

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
import ru.spbgororient.cityorientation.fragments.noQuestSelected.NoQuestSelectedFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {
    private val presenter: MainContract.Presenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        navigation_view.setOnNavigationItemSelectedListener(this)

        navigation_view.selectedItemId = when {
            DataController.instance.quests.questId == "" -> R.id.nav_list_of_quests
            else -> R.id.nav_quest
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("NavigationItemSelected", "item: $item\n" +
                                        "${MyTeamFragment.instance}")
        when (item.itemId) {
            R.id.nav_my_group -> presenter.navigateToMyTeam()
            R.id.nav_list_of_quests -> presenter.navigateToListOfQuests()
            R.id.nav_quest -> presenter.navigateToQuest()
        }
        return true
    }

    override fun showMyTeam() = loadFragment(MyTeamFragment.instance)

    override fun showListOfQuests() = loadFragment(ListOfQuestsFragment.instance)

    override fun showWaitingQuest() = loadFragment(WaitingToStartFragment.instance)

    override fun showTask() = loadFragment(QuestTextFragment.instance)

    override fun showFinishQuest() = loadFragment(FinishFragment.instance)

    override fun showNoQuestSelected() = loadFragment(NoQuestSelectedFragment.instance)

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }
}
