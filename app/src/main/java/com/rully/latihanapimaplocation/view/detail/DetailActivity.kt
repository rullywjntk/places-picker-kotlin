package com.rully.latihanapimaplocation.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val place = intent.getParcelableExtra<Place>(EXTRA_PLACE) as Place
        binding.apply {
            Glide.with(applicationContext)
                .load(place.image)
                .into(ivDetail)
        }
    }

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }
}