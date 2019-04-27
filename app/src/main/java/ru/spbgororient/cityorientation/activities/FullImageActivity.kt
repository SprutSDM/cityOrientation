package ru.spbgororient.cityorientation.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_image.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController

class FullImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        Picasso.with(baseContext)
            .load(DataController.instance.quests.getTask().img)
            .into(image_full_size)
    }
}