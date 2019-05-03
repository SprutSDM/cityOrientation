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
                DataController.instance.quests.let {
                    when {
                        !it.isStarted -> WaitingToStartFragment.instance
                        it.isFinished -> FinishFragment.instance
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

    /*private fun startBackgroundUpdate() {
        val getStateTimer = timer(name = "getStateTimer", initialDelay = 5000, period = 5000) {
            DataController.instance.getStateForTimer(::updateData)
        }
    }*/

    private fun updateData(response: Network.NetworkResponse, questId: String, step: Int, seonds: Long, times: List<Int>, timesComplete: List<Int>) {
        Log.d("NavigationActivity", "questId: $questId, response: $response DCQuestId: ${DataController.instance.quests.questId}")
        if (response != Network.NetworkResponse.OK)
            return

        // Кто-то нажал кнопку покинуть квест
        if (questId == "" && DataController.instance.quests.questId != "") {
            // Кто-то нажал кнопку покинуть квест
            DataController.instance.quests.resetQuest()
            Log.d("NavigationActivity", "Tag: ${fragment.tag}")
            if (fragment.tag == "FragmentQuestText" || fragment.tag == "FragmentQuestTextImg" || fragment.tag == "FragmentWaitingToStart")
                runOnUiThread {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, ListOfQuestsFragment.instance, ListOfQuestsFragment.instance.tag)
                        .commit()
                }
            return
        }

        // Кто-то выбрал квест
        // Возможно, кто-то нажал кнопку покинуть квест и выбрал новый.
        if (questId != "" && (DataController.instance.quests.questId == "" || DataController.instance.quests.questId != questId)) {
            DataController.instance.quests.questId = questId
            DataController.instance.quests.step = step
            DataController.instance.quests.times = times
            DataController.instance.quests.timesComplete = timesComplete
            DataController.instance.loadTasks {
                if (it == Network.NetworkResponse.OK) {
                    // TODO: Добавить переход на экран с ожиданием квеста / заданием.
                }
            }
            return
        }

        // Кто-то ввёл правильный ответ
        if (step != DataController.instance.quests.step) {
            DataController.instance.quests.step = step
            // TODO: Добавить переход к следующему квесту.
            return
        }
    }
}
