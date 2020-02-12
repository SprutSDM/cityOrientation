package ru.spbgororient.cityorientation.fragments.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_finish.*
import ru.spbgororient.cityorientation.App
import ru.spbgororient.cityorientation.R

class FinishFragment : Fragment(), FinishContract.View {

    lateinit var presenter: FinishContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (isAdded) {
            val dataController = (requireActivity().applicationContext as App).dataController
            presenter = FinishPresenter(this, dataController)
        }
        return inflater.inflate(R.layout.fragment_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.viewCreated()
    }

    override fun setQuestImg(url: String) {
        Picasso.with(context)
            .load(url)
            .fit()
            .centerCrop()
            .into(image_preview)
    }

    override fun setQuestTitle(title: String) {
        text_title_of_quest.text = title
    }

    override fun setFinalText(text: String) {
        text_final.text = text
    }

    companion object {
        val instance: FinishFragment by lazy { FinishFragment() }
    }
}