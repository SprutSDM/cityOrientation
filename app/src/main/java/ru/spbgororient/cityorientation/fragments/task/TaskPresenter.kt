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
                    private val mainPresenter: MainContract.Presenter,
                    private val dataController: DataController) : TaskContract.Presenter {
    private lateinit var timer: CountDownTimer
    private var sdf = SimpleDateFormat(view.getTimeFormat(), Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun checkAnswer(answer: String) {
        Log.d("app debug", "answer: $answer")
        if (answer.toLowerCase(Locale.getDefault()) in dataController.quests.getTask().answers) {
            dataController.completeTask(::completeTaskCallback)
        }
    }

    override fun getTip(tipNumber: Int, confirmed: Boolean) {
        val quest = dataController.quests.getQuest() ?: return
        val task = dataController.quests.getTask()
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
        val task = dataController.quests.getTask()
        setupTaskContent(task, dataController.quests.step)
    }

    private fun setupTaskContent(task: Task, taskNumber: Int) {
        dataController.quests.getQuest()?.let { quest ->
            val timeOnStage = quest.startTime * 1000 - (System.currentTimeMillis() + dataController.timeOffset) - dataController.quests.getTimeCompleteLastTask()
            val timeUntilFinish = System.currentTimeMillis() + dataController.timeOffset
            view.updateTimer(sdf.format(timeOnStage), sdf.format(timeUntilFinish))
        }
        view.resetAnswer()

        if (task.img == "") {
            view.showTask(taskNumber + 1, task.content)
        } else {
            view.showTask(taskNumber + 1, task.content, task.img)
        }
        view.hideTips()
        if (dataController.quests.isUsedTip(0) || dataController.quests.getTask().tips[0] == "") {
            view.showTip(0, task.tips[0])
        }
        if (dataController.quests.isUsedTip(1) || dataController.quests.getTask().tips[1] == "") {
            view.showTip(1, task.tips[1])
        }
    }

    override fun start() {
        startTimer()
    }

    override fun stop() {
        timer.cancel()
    }

    override fun activityResult(requestCode: Int, resultCode: Int) {
        dataController.useTip(requestCode) { response, tipNumber ->
            if (response == Network.NetworkResponse.OK) {
                getTip(tipNumber, confirmed = true)
            }
        }
    }

    private fun startTimer() {
        dataController.quests.getQuest()?.let { quest ->
            val time = (quest.duration + quest.startTime) * 1000 - dataController.currentTime
            timer = object: CountDownTimer(time, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    //TODO refactor
                    val timeOnStage = quest.duration - dataController.quests.getTimeCompleteLastTask() * 1000 - millisUntilFinished
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
