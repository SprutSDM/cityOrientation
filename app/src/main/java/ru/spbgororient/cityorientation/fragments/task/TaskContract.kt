package ru.spbgororient.cityorientation.fragments.task

interface TaskContract {

    interface View {
        fun showTask(taskNumber: Int, question: String)
        fun showTask(taskNumber: Int, question: String, imgUrl: String)
        fun showTip(tipNumber: Int, tip: String)
        fun showNoTip(tipNumber: Int)
        fun hideTips()
        fun showCorrectAnswer()
        fun showIncorrectAnswer()
        fun hideKeyboard()
        fun showTipDialog(tipNumber: Int, time: Int)
        fun updateTimer(timeOnStage: String, timeUntilFinish: String)
        fun getTimeFormat(): String
        fun openImage()
        fun resetAnswer()
    }

    interface Presenter {
        fun checkAnswer(answer: String)
        fun getTip(tipNumber: Int, confirmed: Boolean = false)
        fun openImage()
        fun viewCreated()
        fun updateTaskContent()
        fun start()
        fun stop()
    }
}