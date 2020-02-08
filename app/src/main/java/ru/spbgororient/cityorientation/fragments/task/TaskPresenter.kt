package ru.spbgororient.cityorientation.fragments.task

import android.os.CountDownTimer
import android.util.Log
import ru.spbgororient.cityorientation.activities.mainActivity.MainContract
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import ru.spbgororient.cityorientation.quests.Task
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TaskPresenter(private val view: TaskContract.View,
                    private val mainPresenter: MainContract.Presenter) : TaskContract.Presenter {
    private lateinit var timer: CountDownTimer
    private var sdf = SimpleDateFormat(view.getTimeFormat(), Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun checkAnswer(answer: String) {
        Log.d("app debug", "answer: $answer")
        if (answer.toLowerCase(Locale.getDefault()) in DataController.instance.quests.getTask().answers) {
            DataController.instance.completeTask(::completeTaskCallback)
        }
    }

    override fun getTip(tipNumber: Int, confirmed: Boolean) {
        val quest = DataController.instance.quests.getQuest() ?: return
        val task = DataController.instance.quests.getTask()
        if (confirmed) {
            if (task.tips[tipNumber].isEmpty()) {
                view.showNoTip(tipNumber)
            } else {
                view.showTip(tipNumber, task.tips[0])
            }
        } else {
            //TODO refactor
            val tipTime = if (tipNumber == 0) quest.tip_1_time else quest.tip_2_time
            view.showTipDialog(tipNumber, tipTime)
        }
    }

    override fun viewCreated() = updateTaskContent()

    override fun updateTaskContent() {
        val task = DataController.instance.quests.getTask()
        setupTaskContent(task, DataController.instance.quests.step)
    }

    private fun setupTaskContent(task: Task, taskNumber: Int) {
        DataController.instance.quests.getQuest()?.let { quest ->
            val timeOnStage = quest.startTime * 1000 - (System.currentTimeMillis() + DataController.instance.timeOffset) - DataController.instance.quests.getTimeCompleteLastTask()
            val timeUntilFinish = System.currentTimeMillis() + DataController.instance.timeOffset
            view.updateTimer(sdf.format(timeOnStage), sdf.format(timeUntilFinish))
        }
        view.resetAnswer()

        if (task.img == "") {
            view.showTask(taskNumber + 1, task.content)
        } else {
            view.showTask(taskNumber + 1, task.content, task.img)
        }
        view.hideTips()
        if (DataController.instance.quests.isUsedTip(0) || DataController.instance.quests.getTask().tips[0] == "") {
            view.showTip(0, task.tips[0])
        }
        if (DataController.instance.quests.isUsedTip(1) || DataController.instance.quests.getTask().tips[1] == "") {
            view.showTip(1, task.tips[1])
        }
    }

    override fun start() {
        startTimer()
    }

    override fun stop() {
        timer.cancel()
    }

    private fun startTimer() {
        DataController.instance.quests.getQuest()?.let { quest ->
            val time = (quest.duration + quest.startTime) * 1000 - DataController.instance.currentTime
            timer = object: CountDownTimer(time, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    //TODO refactor
                    val timeOnStage = quest.duration - DataController.instance.quests.getTimeCompleteLastTask() * 1000 - millisUntilFinished
                    view.updateTimer(sdf.format(timeOnStage), sdf.format(millisUntilFinished))
                }

                override fun onFinish() {
                    mainPresenter.updateTaskFragment()
                }
            }.start()
        }
    }

    private fun completeTaskCallback(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            mainPresenter.updateTaskFragment()
        }
    }

    override fun openImage() = view.openImage()
}
