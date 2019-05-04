package ru.spbgororient.cityorientation.fragments.quest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_quest_text_img.*
import kotlinx.android.synthetic.main.fragment_quest_text_img.button_check_answer
import kotlinx.android.synthetic.main.fragment_quest_text_img.button_get_tip_1
import kotlinx.android.synthetic.main.fragment_quest_text_img.button_get_tip_2
import kotlinx.android.synthetic.main.fragment_quest_text_img.edit_answer
import kotlinx.android.synthetic.main.fragment_quest_text_img.text_content_quest
import kotlinx.android.synthetic.main.fragment_quest_text_img.text_number_quest
import kotlinx.android.synthetic.main.fragment_quest_text_img.text_time_stage
import kotlinx.android.synthetic.main.fragment_quest_text_img.text_time_until_finish
import kotlinx.android.synthetic.main.fragment_quest_text_img.text_tip_1
import kotlinx.android.synthetic.main.fragment_quest_text_img.text_tip_2
import ru.spbgororient.cityorientation.activities.FullImageActivity
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.NavigationActivity
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import java.text.SimpleDateFormat
import java.util.*

class QuestTextImgFragment: Fragment() {

    private lateinit var timer: CountDownTimer
    val sdf = SimpleDateFormat("HH:mm:ss")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return inflater.inflate(R.layout.fragment_quest_text_img, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_number_quest.text = "Задание №${DataController.instance.quests.step + 1}"
        text_content_quest.text = DataController.instance.quests.getTask().content
        Picasso.with(context)
            .load(Network.URL_IMG + DataController.instance.quests.getTask().img)
            .into(image_quest)
        edit_answer.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (edit_answer.text.toString() != "") {
                        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        if (edit_answer!!.text.toString().toLowerCase() in DataController.instance.quests.getTask().answers) {
                            updateCardFragment()
                            Snackbar.make(activity!!.findViewById(R.id.content_frame), "Правильно!", Snackbar.LENGTH_LONG).show()
                        } else
                            Snackbar.make(activity!!.findViewById(R.id.content_frame), "Ответ неверный!", Snackbar.LENGTH_LONG).show()
                    }
                    true
                }
                else -> false
            }
        }
        button_check_answer.setOnClickListener {
            if (edit_answer.text.toString() != "") {
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                if (edit_answer.text.toString().toLowerCase() in DataController.instance.quests.getTask().answers) {
                    updateCardFragment()
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Правильно!", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Ответ неверный!", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
        button_get_tip_1.setOnClickListener {
            text_tip_1.text = "Подсказка №1: ${DataController.instance.quests.getTask().tips[0]}"
            button_get_tip_1.visibility = View.GONE
        }
        button_get_tip_2.setOnClickListener {
            text_tip_2.text = "Подсказка №2: ${DataController.instance.quests.getTask().tips[1]}"
            button_get_tip_2.visibility = View.GONE
        }
        image_quest.setOnClickListener {
            val intent = Intent(context, FullImageActivity::class.java)
            activity!!.startActivity(intent)
        }

        DataController.instance.quests.getQuest()?.let { quest ->
            text_time_stage.text = sdf.format(quest.startTime * 1000 - (System.currentTimeMillis() + DataController.instance.timeOffset) - DataController.instance.quests.getTimeCompleteLastTask())
            text_time_until_finish.text = sdf.format(System.currentTimeMillis() + DataController.instance.timeOffset)
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

    private fun startTimer() {
        DataController.instance.quests.getQuest()?.let { quest ->
            val time = (quest.duration + quest.startTime) * 1000 - DataController.instance.currentTime
            Log.d("QuestTextImg", "System.currentTimeMillis: ${System.currentTimeMillis()}, quest: ${quest.startTime * 1000}")
            Log.d("QuestTextImg", "time: $time")
            timer = object: CountDownTimer(time, 1000L) {

                override fun onTick(millisUntilFinished: Long) {
                    Log.d("QuestTextImg", "timer is ticking ${sdf.format(millisUntilFinished)}")
                    text_time_stage.text = sdf.format((quest.duration - DataController.instance.quests.getTimeCompleteLastTask()) * 1000 - millisUntilFinished)
                    text_time_until_finish.text = sdf.format(millisUntilFinished)
                }

                override fun onFinish() {
                    Log.d("QuestTextImg", "timer is finished")
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
                DataController.instance.quests.getTask().img == "" -> QuestTextFragment.newInstance()
                else -> newInstance()
            }
            (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
        }
    }

    companion object {
        var instance: QuestTextImgFragment = QuestTextImgFragment()
        const val TAG = "QuestTextImgFragment"

        fun newInstance(): QuestTextImgFragment {
            instance = QuestTextImgFragment()
            return instance
        }
    }
}