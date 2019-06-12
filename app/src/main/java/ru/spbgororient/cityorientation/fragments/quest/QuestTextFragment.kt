package ru.spbgororient.cityorientation.fragments.quest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_quest_text.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.NavigationActivity
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import java.text.SimpleDateFormat
import java.util.*

class QuestTextFragment: Fragment() {

    private lateinit var timer: CountDownTimer
    lateinit var sdf: SimpleDateFormat

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sdf = SimpleDateFormat(getString(R.string.sdf_time))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return inflater.inflate(R.layout.fragment_quest_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_number_quest.text = getString(R.string.task_number, DataController.instance.quests.step + 1)
        text_content_quest.text = DataController.instance.quests.getTask().content
        if (DataController.instance.quests.isUsedTip(0) || DataController.instance.quests.getTask().tips[0] == "")
            showFirstTip()
        if (DataController.instance.quests.isUsedTip(1) || DataController.instance.quests.getTask().tips[1] == "")
            showSecondTip()

        edit_answer.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (edit_answer.text.toString() != "") {
                        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        checkAnswer()
                    }
                    true
                }
                else -> false
            }
        }
        button_check_answer.setOnClickListener {
            if (edit_answer.text.toString() != "") {
                (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                checkAnswer()
            }
        }
        button_get_tip_1.setOnClickListener {
            DataController.instance.quests.getQuest()?.let {
                val fragment = TipDialogFragment()
                val bundle = Bundle()
                fragment.setTargetFragment(this, 0)
                bundle.putInt("time", it.tip_1_time * 1000)
                fragment.arguments = bundle
                fragment.show(fragmentManager, fragment::class.java.name)
            }
        }
        button_get_tip_2.setOnClickListener {
            DataController.instance.quests.getQuest()?.let {
                val fragment = TipDialogFragment()
                val bundle = Bundle()
                fragment.setTargetFragment(this, 1)
                bundle.putInt("time", it.tip_2_time * 1000)
                fragment.arguments = bundle
                fragment.show(fragmentManager, fragment::class.java.name)
            }
        }

        DataController.instance.quests.getQuest()?.let { quest ->
            text_time_stage.text = sdf.format(quest.startTime * 1000 - (System.currentTimeMillis() + DataController.instance.timeOffset) - DataController.instance.quests.getTimeCompleteLastTask())
            text_time_until_finish.text = sdf.format(System.currentTimeMillis() + DataController.instance.timeOffset)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DataController.instance.useTip(requestCode) { response, tipNumber ->
            if (response == Network.NetworkResponse.OK) {
                when (tipNumber) {
                    0 -> showFirstTip()
                    1 -> showSecondTip()
                }
            }
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

    private fun updateCardFragment() {
        DataController.instance.completeTask(::completeTaskCallback)
    }

    private fun checkAnswer() {
        if (edit_answer.text.toString().toLowerCase() in DataController.instance.quests.getTask().answers) {
            updateCardFragment()
            Snackbar.make(activity!!.findViewById(R.id.content_frame), getString(R.string.correct), Snackbar.LENGTH_LONG).show()
        } else
            Snackbar.make(activity!!.findViewById(R.id.content_frame), getString(R.string.wrong), Snackbar.LENGTH_LONG).show()
    }

    private fun showFirstTip() {
        if (DataController.instance.quests.getTask().tips[0] == "")
            text_tip_1.text = getString(R.string.no_tip, 1)
        else
            text_tip_1.text = getString(R.string.tip_number, 1, DataController.instance.quests.getTask().tips[0])
        button_get_tip_1.visibility = View.GONE
    }

    private fun showSecondTip() {
        if (DataController.instance.quests.getTask().tips[1] == "")
            text_tip_2.text = getString(R.string.no_tip, 2)
        else
            text_tip_2.text = getString(R.string.tip_number, 2, DataController.instance.quests.getTask().tips[1])
        button_get_tip_2.visibility = View.GONE
    }

    private fun startTimer() {
        DataController.instance.quests.getQuest()?.let { quest ->
            val time = (quest.duration + quest.startTime) * 1000 - DataController.instance.currentTime
            timer = object: CountDownTimer(time, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    text_time_stage.text = sdf.format((quest.duration - DataController.instance.quests.getTimeCompleteLastTask()) * 1000 - millisUntilFinished)
                    text_time_until_finish.text = sdf.format(millisUntilFinished)
                }

                override fun onFinish() {
                    (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
                }
            }.start()
        }
    }

    private fun stopTimer() {
        timer.cancel()
    }

    private fun completeTaskCallback(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            when {
                DataController.instance.quests.isFinished -> 0 // Действий не надо. Всё будет сделано за нас в NavigationActivity
                DataController.instance.quests.getTask().img == "" -> newInstance()
                else -> QuestTextImgFragment.newInstance()
            }
            (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
        }
    }

    companion object {
        var instance: QuestTextFragment = QuestTextFragment()

        fun newInstance(): QuestTextFragment {
            instance = QuestTextFragment()
            return instance
        }
    }
}