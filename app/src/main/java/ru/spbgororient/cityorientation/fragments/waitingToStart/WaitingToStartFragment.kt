package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_waiting_to_start.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.mainActivity.MainActivity
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import java.text.SimpleDateFormat
import java.util.*

class WaitingToStartFragment: Fragment() {

    private lateinit var timer: CountDownTimer
    lateinit var sdf: SimpleDateFormat

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sdf = SimpleDateFormat(getString(R.string.sdf_time))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return inflater.inflate(R.layout.fragment_waiting_to_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataController.instance.quests.getQuest()?.let { quest ->
            Picasso.with(context)
                .load(Network.URL + quest.img)
                .fit()
                .centerCrop()
                .into(image_preview)
            text_title_of_quest.text = quest.name
            text_start.text = quest.startText

            val time = quest.startTime * 1000 - System.currentTimeMillis()
            text_time_from_start_quest.text = sdf.format(time)
        }
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    private fun startTimer() {
        DataController.instance.quests.getQuest()?.let { quest ->
            val time = quest.startTime * 1000 - (System.currentTimeMillis() + DataController.instance.timeOffset)
            timer = object: CountDownTimer(time, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    text_time_from_start_quest.text = sdf.format(millisUntilFinished)
                }

                override fun onFinish() {
                    (context as MainActivity).navigation_view.selectedItemId = R.id.nav_quest
                }
            }.start()
        }
    }

    private fun stopTimer() {
        DataController.instance.quests.getQuest()?.let {
            timer.cancel()
        }
    }

    companion object {
        var instance: WaitingToStartFragment = WaitingToStartFragment()

        fun newInstance(): WaitingToStartFragment {
            instance = WaitingToStartFragment()
            return instance
        }
    }
}