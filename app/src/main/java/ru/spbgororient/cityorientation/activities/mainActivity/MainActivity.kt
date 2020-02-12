package ru.spbgororient.cityorientation.activities.mainActivity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_navigation.*
import ru.spbgororient.cityorientation.App
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.fragments.listOfQuests.ListOfQuestsFragment
import ru.spbgororient.cityorientation.fragments.myTeam.MyTeamFragment
import ru.spbgororient.cityorientation.fragments.waitingToStart.WaitingToStartFragment
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.fragments.noQuestSelected.NoQuestSelectedFragment
import ru.spbgororient.cityorientation.fragments.task.TaskFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val dataController = (applicationContext as App).dataController
        presenter = MainPresenter(this, dataController)

        navigation_view.setOnNavigationItemSelectedListener(this)

        navigation_view.selectedItemId = when {
            dataController.quests.questId == "" -> R.id.nav_list_of_quests
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

    override fun showTask() {
        if (TaskFragment.instance.isAdded) {
            TaskFragment.instance.presenter.updateTaskContent()
        }
        loadFragment(TaskFragment.instance)
    }

    override fun showFinishQuest() = loadFragment(FinishFragment.instance)

    override fun showNoQuestSelected() = loadFragment(NoQuestSelectedFragment.instance)

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }
}
