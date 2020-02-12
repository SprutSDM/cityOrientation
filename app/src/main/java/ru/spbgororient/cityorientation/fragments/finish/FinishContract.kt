package ru.spbgororient.cityorientation.fragments.finish

interface FinishContract {
    interface View {
        fun setQuestImg(url: String)
        fun setQuestTitle(title: String)
        fun setFinalText(text: String)
    }

    interface Presenter {
        fun viewCreated()
    }
}
