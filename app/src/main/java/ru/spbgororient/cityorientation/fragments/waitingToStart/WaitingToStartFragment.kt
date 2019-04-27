package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_waiting_to_start.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextImgFragment

class WaitingToStartFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_waiting_to_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_login.setOnClickListener {
            val fragment = if (DataController.instance.quests.step == DataController.instance.quests.listOfTasks.size)
                FinishFragment.instance
            else if (DataController.instance.quests.getTask().img == "")
                QuestTextFragment.newInstance()
            else
                QuestTextImgFragment.newInstance()
            fragmentManager!!.beginTransaction().replace(R.id.content_frame, fragment).commit()
        }
    }

    companion object {
        var instance: WaitingToStartFragment = WaitingToStartFragment()
        val TAG = "WaitingToStartFragment"

        fun newInstance(): WaitingToStartFragment {
            instance = WaitingToStartFragment()
            return instance
        }
    }
}