package com.rully.latihanapimaplocation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rully.latihanapimaplocation.adapter.PlaceAdapter
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ActivityMainBinding
import com.rully.latihanapimaplocation.helper.DatabaseHelper
import com.rully.latihanapimaplocation.view.add.PlaceActivity
import com.rully.latihanapimaplocation.view.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PlaceAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        adapter = PlaceAdapter()
        getData()

        adapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                showSelectedData(data)
            }
        })

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, PlaceActivity::class.java))
        }
    }

    private fun getData() {
        dbHelper.getAll().observe(this, { listPlace ->
            if (listPlace != null) {
                adapter.setList(listPlace)
                tvNoData(false)
            }
        })

        binding.rvData.isNestedScrollingEnabled = false
        binding.rvData.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvData.setHasFixedSize(true)
        binding.rvData.adapter = adapter
    }

    private fun showSelectedData(data: Place) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_PLACE, data)
        startActivity(intent)
    }

    private fun tvNoData(state: Boolean) {
        if (state) {
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.tvNoData.visibility = View.GONE
        }
    }
}