package com.rully.latihanapimaplocation.view.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rully.latihanapimaplocation.R
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ActivityMapBinding
import com.rully.latihanapimaplocation.view.detail.DetailActivity

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding

    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        place = intent.getParcelableExtra<Place>(DetailActivity.EXTRA_PLACE) as Place
        binding.tvTitle.text = place.title

        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(place.latitude, place.longitude)
        googleMap.addMarker(MarkerOptions().position(position).title(place.location))
        val zoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(zoom)
    }
}