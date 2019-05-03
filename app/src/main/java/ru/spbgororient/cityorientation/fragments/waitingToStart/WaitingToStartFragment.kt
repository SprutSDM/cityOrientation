package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_waiting_to_start.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.NavigationActivity
import ru.spbgororient.cityorientation.dataController.DataController
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
        button_login.setOnClickListener {
            DataController.instance.quests.isStarted = true // TODO: убрать это от сюда
            (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
        }
        DataController.instance.quests.getQuest()?.let { quest ->
            val time = quest.seconds * 1000 - System.currentTimeMillis()
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
            val time = quest.seconds * 1000 - (System.currentTimeMillis() + DataController.instance.timeOffset)
            Log.d("WaitingToStart", "System.currentTimeMillis: ${System.currentTimeMillis()}, quest: ${quest.seconds * 1000}")
            Log.d("WaitingToStart", "time: $time")
            Log.d("WaitingToStart", "timeOffset: ${DataController.instance.timeOffset}")
            timer = object: CountDownTimer(time, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    Log.d("WaitingToStartFragment", "timer is ticking ${sdf.format(millisUntilFinished)}")
                    text_time_from_start_quest.text = sdf.format(millisUntilFinished)
                }

                override fun onFinish() {
                    Log.d("WaitingToStartFragment", "timer is finished")
                    DataController.instance.quests.isStarted = true
                    (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
                }
            }.start()
        }
    }

    private fun stopTimer() {
        timer.cancel()
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