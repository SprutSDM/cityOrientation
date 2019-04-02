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
import kotlinx.android.synthetic.main.fragment_quest_text.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.questsController.DataController

class QuestTextFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quest_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_number_quest.text = "Задание №${DataController.instance.step + 1}"
        text_content_quest.text = DataController.instance.getTask().content
        edit_answer.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (edit_answer.text.toString() != "") {
                        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        if (edit_answer!!.text.toString().toLowerCase() in DataController.instance.getTask().answers) {
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
                if (edit_answer.text.toString().toLowerCase() in DataController.instance.getTask().answers) {
                    updateCardFragment()
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Правильно!", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Ответ неверный!", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
        button_get_tip_1.setOnClickListener {
            text_tip_1.text = "Подсказка №1: ${DataController.instance.getTask().tips[0]}"
            button_get_tip_1.visibility = View.GONE
        }
        button_get_tip_2.setOnClickListener {
            text_tip_2.text = "Подсказка №2: ${DataController.instance.getTask().tips[1]}"
            button_get_tip_2.visibility = View.GONE
        }
    }

    fun updateCardFragment() {
        lateinit var fragment: Fragment
        DataController.instance.completeTask()
        DataController.instance.step += 1
        if (DataController.instance.step >= DataController.instance.listOfTasks.size)
            fragment = FinishFragment.instance
        else if (DataController.instance.getTask().img == "")
            fragment = QuestTextFragment.newInstance()
        else
            fragment = QuestTextImgFragment.newInstance()
        fragmentManager!!.beginTransaction().replace(R.id.content_frame, fragment).commit()
    }

    companion object {
        var instance: QuestTextFragment = QuestTextFragment()
        val TAG = "QuestTextFragment"

        fun newInstance(): QuestTextFragment {
            instance = QuestTextFragment()
            return instance
        }
    }
}