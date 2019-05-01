package ru.spbgororient.cityorientation.fragments.quest

import android.content.Context
import android.os.Bundle
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
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.network.Network

class QuestTextFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quest_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_number_quest.text = "Задание №${DataController.instance.quests.step + 1}"
        text_content_quest.text = DataController.instance.quests.getTask().content
        edit_answer.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (edit_answer.text.toString() != "") {
                        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        if (edit_answer!!.text.toString().toLowerCase() in DataController.instance.quests.getTask().answers) {
                            updateCardFragment()
                            Snackbar.make(
                                activity!!.findViewById(R.id.content_frame), "Правильно!", Snackbar.LENGTH_LONG).show()
                        } else
                            Snackbar.make(activity!!.findViewById(R.id.content_frame), "Ответ неверный!", Snackbar.LENGTH_LONG).show()
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
        button_check_answer.setOnClickListener {
            if (edit_answer.text.toString() == "")

            else {
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                if (edit_answer.text.toString().toLowerCase() in DataController.instance.quests.getTask().answers) {
                    updateCardFragment()
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Правильно!", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Ответ неверный!", Snackbar.LENGTH_LONG).show()
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
    }

    private fun updateCardFragment() {
        DataController.instance.completeTask(::completeTaskCallback)
    }

    private fun completeTaskCallback(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            when {
                DataController.instance.quests.isFinished -> return
                DataController.instance.quests.getTask().img == "" -> newInstance()
                else -> QuestTextImgFragment.newInstance()
            }
            (context as NavigationActivity).navigation_view.selectedItemId = R.id.nav_quest
        }
    }

    companion object {
        var instance: QuestTextFragment = QuestTextFragment()
        const val TAG = "QuestTextFragment"

        fun newInstance(): QuestTextFragment {
            instance = QuestTextFragment()
            return instance
        }
    }
}