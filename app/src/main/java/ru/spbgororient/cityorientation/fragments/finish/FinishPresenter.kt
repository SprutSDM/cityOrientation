package ru.spbgororient.cityorientation.fragments.finish

import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network

class FinishPresenter(private val view: FinishContract.View,
                      private val dataController: DataController) : FinishContract.Presenter {
    override fun viewCreated() {
        dataController.quests.getQuest()?.let {
            view.setQuestImg(Network.URL + it.img)
            view.setFinalText(it.finalText)
            view.setQuestTitle(it.name)
        }
    }
}
