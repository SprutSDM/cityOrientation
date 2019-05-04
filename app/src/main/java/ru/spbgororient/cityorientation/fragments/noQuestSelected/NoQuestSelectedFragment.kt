package ru.spbgororient.cityorientation.fragments.finish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spbgororient.cityorientation.R

class NoQuestSelectedFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_quest_selected, container, false)
    }

    companion object {
        val instance: NoQuestSelectedFragment by lazy { NoQuestSelectedFragment() }
        const val TAG = "NoQuestSelected"
    }
}