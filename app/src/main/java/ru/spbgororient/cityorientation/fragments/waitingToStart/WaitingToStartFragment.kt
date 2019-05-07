package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_waiting_to_start.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.NavigationActivity
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import java.text.SimpleDateFormat
import java.util.*

class WaitingToStartFragment: Fragment() {

    private lateinit var timer: CountDownTimer
    val sdf = SimpleDateFormat("HH:mm:ss")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return inflater.inflate(R.layout.fragment_waiting_to_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataController.instance.quests.getQuest()?.let { quest ->
            Picasso.with(context)
                .load(Network.URL + quest.img)
                .fit()
                .into(image_preview_waiting_quest)
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
            Log.d("WaitingToStart", "System.currentTimeMillis: ${System.currentTimeMillis()}, quest: ${quest.startTime * 1000}")
            Log.d("WaitingToStart", "time: $time")
            Log.d("WaitingToStart", "timeOffset: ${DataController.instance.timeOffset}")
            timer = object: CountDownTimer(time, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    Log.d("WaitingToStartFragment", "timer is ticking ${sdf.format(millisUntilFinished)}")
                    text_time_from_start_quest.text = sdf.format(millisUntilFinished)
                }

                override fun onFinish() {
                    Log.d("WaitingToStartFragment", "timer is finished")
                    (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
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
        const val TAG = "WaitingToStartFragment"

        fun newInstance(): WaitingToStartFragment {
            instance = WaitingToStartFragment()
            return instance
        }
    }
}