package ru.spbgororient.cityorientation.fragments.noQuestSelected

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spbgororient.cityorientation.R

class NoQuestSelectedFragment: androidx.fragment.app.Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_quest_selected, container, false)
    }

    companion object {
        val instance: NoQuestSelectedFragment by lazy { NoQuestSelectedFragment() }
    }
}