package com.rully.latihanapimaplocation.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ActivityDetailBinding
import com.rully.latihanapimaplocation.helper.DatabaseHelper
import com.rully.latihanapimaplocation.utils.BottomSheetFragment

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgBtnMenu.setOnClickListener {
            showBottomDialog()
        }

        val place = intent.getParcelableExtra<Place>(EXTRA_PLACE) as Place
        binding.apply {
            tvTitle.text = place.title
            tvLocation.text = place.location
            tvDesc.text = place.description
            tvDate.text = place.date
            Glide.with(applicationContext)
                .load(place.image)
                .into(ivDetail)
        }
    }

    private fun showBottomDialog() {
        val modalBottomSheet = BottomSheetFragment()
        modalBottomSheet.show(supportFragmentManager, BottomSheetFragment.TAG)
    }

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }
}