package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.content.Context
import android.provider.ContactsContract
import android.support.v4.app.FragmentManager
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.NavigationActivity
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.fragments.myTeam.MyTeamFragment
import ru.spbgororient.cityorientation.fragments.waitingToStart.WaitingToStartFragment
import ru.spbgororient.cityorientation.network.Network
import ru.spbgororient.cityorientation.quests.Quest
import java.text.SimpleDateFormat
import java.util.*

class Adapter(val context: Context,
              private val fragmentManager: FragmentManager) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val quests: List<Quest> = DataController.instance.quests.getListOfQuests()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.card_view_quest_in_list, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        Picasso.with(context)
            .load(Network.URL + quests[i].img)
            .fit()
            .centerCrop()
            .into(viewHolder.questCard.findViewById<ImageView>(R.id.image_preview_quest))
        viewHolder.questCard.findViewById<TextView>(R.id.text_number_quest).text = quests[i].name
        viewHolder.questCard.findViewById<TextView>(R.id.text_place_start).text = quests[i].place
        val date = Date(quests[i].startTime * 1000L)
        val sdf = SimpleDateFormat(context.getString(R.string.sdf_full_time), Locale("ru"))
        viewHolder.questCard.findViewById<TextView>(R.id.text_date_start).text = sdf.format(date)
        viewHolder.questCard.findViewById<Button>(R.id.but_apply).setOnClickListener {
            DataController.instance.joinToQuest(quests[i].questId, ::callbackApply)
        }
        if (DataController.instance.quests.questId != "")
            viewHolder.questCard.findViewById<Button>(R.id.but_apply).isEnabled = false
    }

    /**
     * Вызывается, когда приходит ответ на запрос участия в квесте.
     */
    private fun callbackApply(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            MyTeamFragment.instance.setVisibleLeaveButton()
            notifyDataSetChanged()
            DataController.instance.loadTasks(::callbackListOfTasks)
        }
    }

    /**
     * Вызывается, когда приходит список заданий.
     * Переходит на экран с квестом.
     */
    private fun callbackListOfTasks(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            WaitingToStartFragment.newInstance() // TODO: разобраться, нужно ли это
            (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
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