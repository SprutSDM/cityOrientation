package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_waiting_to_start.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextFragment
import ru.spbgororient.cityorientation.fragments.quest.QuestTextImgFragment
import ru.spbgororient.cityorientation.questsController.DataController

class WaitingToStartFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_waiting_to_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        butContinue.setOnClickListener {
            lateinit var fragment: Fragment
            if (DataController.instance.step == DataController.instance.listOfTasks.size)
                fragment = FinishFragment.instance
            else if (DataController.instance.getTask().img == "")
                fragment = QuestTextFragment.newInstance()
            else
                fragment = QuestTextImgFragment.newInstance()
            fragmentManager!!.beginTransaction().replace(R.id.content_frame, fragment).commit()
        }
    }

    companion object {
        var instance: WaitingToStartFragment = WaitingToStartFragment()

        fun newInstance(): WaitingToStartFragment {
            instance = WaitingToStartFragment()
            return instance
        }
    }
}