package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spbgororient.cityorientation.R

class ListOfQuestsFragment: Fragment() {
    private lateinit var adapter: Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var thisView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_list_of_quests, container, false)
        recyclerView = thisView.findViewById(R.id.recycler_view)

        adapter = Adapter(
            thisView.context,
            fragmentManager
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(thisView.context)
        return thisView
    }

    companion object {
        val instance: ListOfQuestsFragment by lazy { ListOfQuestsFragment() }
        val TAG = "ListOfQuestsFragment"
    }
}