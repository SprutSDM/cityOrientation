package ru.spbgororient.cityorientation.fragments.quest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spbgororient.cityorientation.R

class QuestViewFragment(): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quest_view, container, false)
        val fragment = QuestTextImgFragment.newInstance()
        fragmentManager!!.beginTransaction().replace(R.id.questFragment, fragment).commit()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        fun newInstance(): QuestViewFragment {
            return QuestViewFragment()
        }
    }
}