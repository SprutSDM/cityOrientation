package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.spbgororient.cityorientation.App
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.fragments.myTeam.MyTeamFragment
import ru.spbgororient.cityorientation.fragments.waitingToStart.WaitingToStartFragment
import ru.spbgororient.cityorientation.network.Network
import ru.spbgororient.cityorientation.quests.Quest
import java.text.SimpleDateFormat
import java.util.*

class Adapter(val context: Context,
              private val fragmentManager: FragmentManager?) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val quests: List<Quest> = DataController.instance.quests.listOfQuests

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.card_view_quest_in_list, viewGroup, false)
        return ViewHolder(v)
    }

    private fun callbackListOfTasks(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            val fragment = WaitingToStartFragment.newInstance()
            fragmentManager!!.beginTransaction().replace(R.id.content_frame, fragment).commit()
        }
    }

    private fun callbackApply(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            MyTeamFragment.instance.setVisibleLeaveButton()
            DataController.instance.loadTasks(::callbackListOfTasks)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.questCard.findViewById<TextView>(R.id.text_number_quest).text = quests[i].name
        viewHolder.questCard.findViewById<TextView>(R.id.text_place_start).text = quests[i].place
        val date = Date((quests[i].date * 86400 + quests[i].time) * 1000L)
        val sdf = SimpleDateFormat("MMM, dd в HH:mm", Locale("ru"))
        viewHolder.questCard.findViewById<TextView>(R.id.text_date_start).text = sdf.format(date)
        viewHolder.questCard.findViewById<TextView>(R.id.text_amount_cp).text = quests[i].amountOfCp.toString()
        viewHolder.questCard.findViewById<Button>(R.id.but_apply).setOnClickListener {
            DataController.instance.joinToQuest(i, ::callbackApply)
        }

    }

    override fun getItemCount(): Int {
        return quests.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val questCard: CardView = itemView.findViewById(R.id.card_quest_view_preview)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {

        }

    }
}