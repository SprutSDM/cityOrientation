package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.questsController.Quest
import ru.spbgororient.cityorientation.questsController.DataController

class Adapter(val context: Context,
              val questsController: DataController) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private val quests: List<Quest>

    init {
        this.quests = questsController.listOfQuests
        Log.d("ссссс", "initAdapter")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.quest_in_list_card_view, viewGroup, false)
        Log.d("ссссс", "createViewHolder")
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.questCard.findViewById<TextView>(R.id.numberOfQuest).text = quests[i].name
        viewHolder.questCard.findViewById<TextView>(R.id.dateToStart).text = quests[i].date
        viewHolder.questCard.findViewById<TextView>(R.id.amountOfCp).text = quests[i].amountOfCp.toString()
        Log.d("ссссс", "bindViewHolder " + i.toString())
    }

    override fun getItemCount(): Int {
        Log.d("ссссс", "getItemCountAdapter")
        return quests.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val questCard: CardView = itemView.findViewById(R.id.quest_card_view)

        init {
            Log.d("ссссс", "initViewHolder")
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {

        }

    }
}