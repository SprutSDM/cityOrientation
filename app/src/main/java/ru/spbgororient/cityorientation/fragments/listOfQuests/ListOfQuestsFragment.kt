package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.questsController.DataController

class ListOfQuestsFragment: Fragment() {

    lateinit var adapter: Adapter
    lateinit var recyclerView: RecyclerView
    lateinit var thisView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_list_of_quests, container, false)
        recyclerView = thisView.findViewById(R.id.recyclerList)


        adapter = Adapter(
            thisView.context,
            DataController.instance,
            fragmentManager
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(thisView.context)
        return thisView
    }

    companion object {
        val instance: ListOfQuestsFragment by lazy { ListOfQuestsFragment() }
    }
}