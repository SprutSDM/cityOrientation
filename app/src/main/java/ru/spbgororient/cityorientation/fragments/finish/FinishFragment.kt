package ru.spbgororient.cityorientation.fragments.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_finish.*
import ru.spbgororient.cityorientation.App
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.network.Network

class FinishFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as App).dataController.quests.getQuest()?.let { quest ->
            Picasso.with(context)
                .load(Network.URL + quest.img)
                .fit()
                .centerCrop()
                .into(image_preview)
            text_title_of_quest.text = quest.name
            text_final.text = quest.finalText
        }
    }

    companion object {
        val instance: FinishFragment by lazy { FinishFragment() }
    }
}