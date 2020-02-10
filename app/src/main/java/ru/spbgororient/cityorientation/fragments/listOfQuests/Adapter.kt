package ru.spbgororient.cityorientation.fragments.listOfQuests

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import ru.spbgororient.cityorientation.quests.Quest
import java.text.SimpleDateFormat
import java.util.*

class Adapter(private val context: Context,
              quests: List<Quest>,
              private val itemListener: QuestItemListener) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var quests: List<Quest> = quests
        set(quests) {
            field = quests
            notifyDataSetChanged()
        }

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
            itemListener.onJoinToQuestClick(quests[i])
        }
        if (DataController.instance.quests.questId != "") {
            viewHolder.questCard.findViewById<Button>(R.id.but_apply).isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return quests.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questCard: CardView = itemView.findViewById(R.id.card_quest_view_preview)
    }
}