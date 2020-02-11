package ru.spbgororient.cityorientation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_image.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network

class FullImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        Picasso.with(baseContext)
            .load(Network.URL_IMG + DataController.instance.quests.getTask().img)
            .into(image_full_size)
    }
}