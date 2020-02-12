package ru.spbgororient.cityorientation.fragments.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_task.*
import ru.spbgororient.cityorientation.App
import ru.spbgororient.cityorientation.activities.FullImageActivity
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.mainActivity.MainActivity

class TaskFragment: Fragment(), TaskContract.View {

    lateinit var presenter: TaskContract.Presenter

    private lateinit var firstTip: TipView
    private lateinit var secondTip: TipView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.let {
            if (it is MainActivity) {
                presenter = TaskPresenter(this, it.presenter, (it.applicationContext as App).dataController)
            }
        }
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstTip = TipView(card_tip_1.findViewById(R.id.text_tip), card_tip_1.findViewById(R.id.button_get_tip))
        secondTip = TipView(card_tip_2.findViewById(R.id.text_tip), card_tip_2.findViewById(R.id.button_get_tip))

        edit_answer.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (edit_answer.text.toString() != "") {
                        Log.d("app debug", "pressed enter")
                        presenter.checkAnswer(edit_answer.text.toString())
                    }
                    true
                }
                else -> false
            }
        }
        button_check_answer.setOnClickListener {
            if (edit_answer.text.toString() != "") {
                Log.d("app debug", "pressed button")
                presenter.checkAnswer(edit_answer.text.toString())
            }
        }

        firstTip.button.setOnClickListener {
            presenter.getTip(0)
        }
        secondTip.button.setOnClickListener {
            presenter.getTip(1)
        }

        image_quest.setOnClickListener {
            presenter.openImage()
        }
        presenter.viewCreated()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.activityResult(requestCode, resultCode)
    }

    override fun showTask(taskNumber: Int, question: String) {
        text_number_quest.text = getString(R.string.task_number, taskNumber)
        text_content_quest.text = question
        image_quest.visibility = View.GONE
    }

    override fun showTask(taskNumber: Int, question: String, imgUrl: String) {
        text_number_quest.text = getString(R.string.task_number, taskNumber)
        text_content_quest.text = question
        image_quest.visibility = View.VISIBLE
        Picasso.with(context)
            .load(imgUrl)
            .into(image_quest)
    }

    override fun showTip(tipNumber: Int, tip: String) {
        setupTipView(getTip(tipNumber), getString(R.string.tip_number, tipNumber + 1, tip), buttonVisible = false)
    }

    override fun showNoTip(tipNumber: Int) {
        setupTipView(getTip(tipNumber), getString(R.string.no_tip, tipNumber + 1), buttonVisible = false)
    }

    override fun hideTips() {
        setupTipView(getTip(0), getString(R.string.get_tip_number, 1), buttonVisible = true)
        setupTipView(getTip(1), getString(R.string.get_tip_number, 2), buttonVisible = true)
    }

    private fun setupTipView(tipView: TipView, text: String, buttonVisible: Boolean) {
        tipView.textView.text = text
        tipView.button.visibility = if (buttonVisible) View.VISIBLE else View.GONE
    }

    private fun getTip(tipNumber: Int) = if (tipNumber == 0) firstTip else secondTip

    override fun showCorrectAnswer() {
        showSnackbar(R.string.correct_answer)
    }

    override fun showIncorrectAnswer() {
        showSnackbar(R.string.incorrect_answer)
    }

    private fun showSnackbar(@StringRes messageId: Int) {
        activity?.let { a ->
            Snackbar.make(a.findViewById(R.id.content_frame), getString(messageId), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun hideKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun showTipDialog(tipNumber: Int, time: Int) {
        val fragment = TipDialogFragment()
        val bundle = Bundle()
        fragment.setTargetFragment(this, tipNumber)
        bundle.putInt("time", time)
        fragment.arguments = bundle
        activity?.let {
            fragment.show(it.supportFragmentManager, fragment::class.java.name)
        }
    }

    override fun updateTimer(timeOnStage: String, timeUntilFinish: String) {
        text_time_stage.text = timeOnStage
        text_time_until_finish.text = timeUntilFinish
    }

    override fun getTimeFormat() = getString(R.string.sdf_time)

    override fun openImage() {
        val intent = Intent(context, FullImageActivity::class.java)
        activity?.startActivity(intent)
    }

    override fun resetAnswer() = edit_answer.setText("")

    data class TipView(
        val textView: TextView,
        val button: Button
    )

    companion object {
        var instance: TaskFragment = TaskFragment()
    }
}
