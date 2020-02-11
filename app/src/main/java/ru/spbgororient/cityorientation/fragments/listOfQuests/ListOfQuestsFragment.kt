package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list_of_quests.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.mainActivity.MainActivity
import ru.spbgororient.cityorientation.quests.Quest

class ListOfQuestsFragment: androidx.fragment.app.Fragment(), ListOfQuestsContract.View {
    private lateinit var adapter: Adapter
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var thisView: View

    private lateinit var presenter: ListOfQuestsContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.let {
            if (it is MainActivity) {
                presenter = ListOfQuestsPresenter(this, it.presenter)
            }
        }
        thisView = inflater.inflate(R.layout.fragment_list_of_quests, container, false)
        recyclerView = thisView.findViewById(R.id.recycler_view)

        val itemListener: QuestItemListener = object : QuestItemListener {
            override fun onJoinToQuestClick(clickedQuest: Quest) {
                presenter.joinToQuest(clickedQuest)
            }
        }

        adapter = Adapter(thisView.context, emptyList(), itemListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(thisView.context)
        return thisView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun showQuests(quests: List<Quest>) {
        adapter.quests = quests
        text_no_quests.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showNoQuests() {
        adapter.quests = emptyList()
        text_no_quests.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    companion object {
        val instance: ListOfQuestsFragment by lazy { ListOfQuestsFragment() }
    }
}
