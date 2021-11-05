package com.rully.latihanapimaplocation.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amitshekhar.DebugDB
import com.rully.latihanapimaplocation.adapter.PlaceAdapter
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ActivityMainBinding
import com.rully.latihanapimaplocation.helper.DatabaseHelper
import com.rully.latihanapimaplocation.view.add.AddActivity
import com.rully.latihanapimaplocation.view.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PlaceAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DebugDB.getAddressLog()

        dbHelper = DatabaseHelper(this)
        dbHelper.getAll().observe(this, { listPlace ->
            if (listPlace != null) {
                adapter.setList(listPlace)
            }
        })
        adapter = PlaceAdapter()
        adapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                showSelectedData(data)
            }

        })
        binding.rvData.isNestedScrollingEnabled = false
        binding.rvData.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvData.setHasFixedSize(true)
        binding.rvData.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    private fun showSelectedData(data: Place) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_PLACE, data)
        startActivity(intent)
    }
}