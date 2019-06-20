package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list_of_quests.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController

class ListOfQuestsFragment: Fragment() {
    private lateinit var adapter: Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var thisView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_list_of_quests, container, false)
        recyclerView = thisView.findViewById(R.id.recycler_view)

        adapter = Adapter(
            thisView.context,
            fragmentManager!!
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(thisView.context)
        return thisView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (DataController.instance.quests.getListOfQuests().size == 0)
            text.visibility = View.VISIBLE
    }

    fun notifyDataSetChanged() {
        if (::adapter.isInitialized)
            adapter.notifyDataSetChanged()
    }

    companion object {
        val instance: ListOfQuestsFragment by lazy { ListOfQuestsFragment() }
    }
}