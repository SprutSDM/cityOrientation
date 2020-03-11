package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.CountDownTimer
import ru.spbgororient.cityorientation.activities.mainActivity.MainContract
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class WaitingToStartPresenter(private val view: WaitingToStartContract.View,
                              private val mainPresenter: MainContract.Presenter,
                              private val dataController: DataController): WaitingToStartContract.Presenter {
    private lateinit var timer: CountDownTimer
    private var sdf = SimpleDateFormat(view.getTimeFormat(), Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun viewCreated() {
        val quest = dataController.quests.getQuest() ?: return
        val time = quest.startTime * 1000 - System.currentTimeMillis()
        view.updateTimer(sdf.format(time))
        view.setQuestLogo(Network.URL + quest.img)
        view.setQuestTitle(quest.name)
        //TODO refactor rename
        view.setWelcomeText(quest.startText)
    }

    override fun start() {
        startTimer()
    }

    override fun stop() {
        timer.cancel()
    }

    private fun startTimer() {
        dataController.quests.getQuest()?.let { quest ->
            val time = quest.startTime * 1000 - (System.currentTimeMillis() + dataController.timeOffset)
            timer = object: CountDownTimer(time, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    view.updateTimer(sdf.format(millisUntilFinished))
                }

                override fun onFinish() {
                    mainPresenter.updateTaskFragment()
                }
            }.start()
        }
    }
}
