package ru.spbgororient.cityorientation.fragments.quest

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_quest_text.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.fragments.finish.FinishFragment
import ru.spbgororient.cityorientation.fragments.listOfQuests.ListOfQuestsFragment
import ru.spbgororient.cityorientation.questsController.DataController

class QuestTextImgFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quest_text_img, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberOfQuest.text = "Задание №${DataController.instance.step + 1}"
        contentOfQuest.text = DataController.instance.listOfTasks[DataController.instance.step].content

        checkAnswer.setOnClickListener {
            lateinit var fragment: Fragment
            if (DataController.instance.getTask().answers.contains(checkAnswer!!.text.toString())) {
                DataController.instance.step += 1
                if (DataController.instance.questId == "Quest ID")
                    fragment = ListOfQuestsFragment.instance
                else if (DataController.instance.step >= DataController.instance.listOfTasks.size)
                    fragment = FinishFragment.instance
                else if (DataController.instance.getTask().img == "")
                    fragment = QuestTextFragment.newInstance()
                else
                    fragment = QuestTextImgFragment.newInstance()
                fragmentManager!!.beginTransaction().replace(R.id.content_frame, fragment).commit()
                Snackbar.make(activity!!.findViewById(R.id.content_frame), "Правильно!", Snackbar.LENGTH_LONG).show()
            } else
                Snackbar.make(activity!!.findViewById(R.id.content_frame), "Ответ неверный!", Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        var instance: QuestTextImgFragment = QuestTextImgFragment()

        fun newInstance(): QuestTextImgFragment {
            instance = QuestTextImgFragment()
            return instance
        }
    }
}