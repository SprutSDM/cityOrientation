package ru.spbgororient.cityorientation.fragments.waitingToStart

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_waiting_to_start.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.mainActivity.MainActivity
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import java.text.SimpleDateFormat
import java.util.*

class WaitingToStartFragment: Fragment(), WaitingToStartContract.View {

    private lateinit var presenter: WaitingToStartContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.let {
            if (it is MainActivity) {
                presenter = WaitingToStartPresenter(this, it.presenter)
            }
        }
        return inflater.inflate(R.layout.fragment_waiting_to_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun updateTimer(timeUntilStart: String) {
        text_time_from_start_quest.text = timeUntilStart
    }

    override fun setQuestLogo(url: String) {
        Picasso.with(context)
            .load(url)
            .fit()
            .centerCrop()
            .into(image_preview)
    }

    override fun setQuestTitle(title: String) {
        text_title_of_quest.text = title
    }

    override fun setWelcomeText(text: String) {
        text_start.text = text
    }

    override fun getTimeFormat(): String = getString(R.string.sdf_time)

    companion object {
        var instance: WaitingToStartFragment = WaitingToStartFragment()

        fun newInstance(): WaitingToStartFragment {
            instance = WaitingToStartFragment()
            return instance
        }
    }
}
