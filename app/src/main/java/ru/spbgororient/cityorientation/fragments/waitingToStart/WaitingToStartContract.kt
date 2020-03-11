package ru.spbgororient.cityorientation.fragments.waitingToStart

interface WaitingToStartContract {
    interface View {
        fun updateTimer(timeUntilStart: String)
        fun setQuestLogo(url: String)
        fun setQuestTitle(title: String)
        fun setWelcomeText(text: String)
        fun getTimeFormat(): String
    }

    interface Presenter {
        fun viewCreated()
        fun start()
        fun stop()
    }
}