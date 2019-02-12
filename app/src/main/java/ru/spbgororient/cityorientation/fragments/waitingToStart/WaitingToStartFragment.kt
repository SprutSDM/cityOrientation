package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_waiting_to_start.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.fragments.quest.QuestViewFragment

class WaitingToStartFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_waiting_to_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        butContinue.setOnClickListener {
            val questFragment = QuestViewFragment.instanse
            fragmentManager!!.beginTransaction().replace(R.id.content_frame, questFragment).commit()
        }
    }

    companion object {
        val instanse: WaitingToStartFragment by lazy { WaitingToStartFragment() }
    }
}