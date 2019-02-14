package ru.spbgororient.cityorientation.fragments.quest

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_quest_text.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.fragments.listOfQuests.ListOfQuestsFragment
import ru.spbgororient.cityorientation.questsController.DataController

class QuestTextFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quest_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberOfQuest.text = "Задание №${DataController.instance.step + 1}"
        contentOfQuest.text = DataController.instance.listOfTasks[DataController.instance.step].content
        inputAnswer.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (inputAnswer.text.toString() != "") {
                        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        if (checkAnswer!!.text.toString() in DataController.instance.getTask().answers) {
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
        checkAnswer.setOnClickListener {
            if (inputAnswer.text.toString() == "")

            else {
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                if (inputAnswer.text.toString() in DataController.instance.getTask().answers) {
                    updateCardFragment()
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Правильно!", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(activity!!.findViewById(R.id.content_frame), "Ответ неверный!", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun updateCardFragment() {
        lateinit var fragment: Fragment
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

        fun newInstance(): QuestTextFragment {
            instance = QuestTextFragment()
            return instance
        }
    }
}