package com.rully.latihanapimaplocation.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rully.latihanapimaplocation.R
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ActivityDetailBinding
import com.rully.latihanapimaplocation.helper.DatabaseHelper
import com.rully.latihanapimaplocation.view.add.PlaceActivity

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgBtnMenu.setOnClickListener {
            showBottomDialog()
        }
        place = intent.getParcelableExtra<Place>(EXTRA_PLACE) as Place

        getDetailData()
        binding.ibSend.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ibSend -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, place.title)
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(place.image))
                intent.type = "image/jpg"
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(intent, "Share images"))
            }
        }
    }

    private fun getDetailData() {
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
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_content)

        val tvShare = dialog.findViewById<TextView>(R.id.tvShare)
        val tvEdit = dialog.findViewById<TextView>(R.id.tvEdit)
        val tvDelete = dialog.findViewById<TextView>(R.id.tvDelete)

        tvShare?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, place.title)
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(place.image))
            intent.type = "image/jpg"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Share images"))
        }

        tvEdit?.setOnClickListener {
            editData()
            finish()
        }

        tvDelete?.setOnClickListener {
            showDeleteDialog()
        }
        dialog.show()
    }

    private fun editData() {
        val place = intent.getParcelableExtra<Place>(EXTRA_PLACE)
        val intent = Intent(this, PlaceActivity::class.java)
        intent.putExtra(PlaceActivity.EXTRA_PLACE, place)
        startActivity(intent)
    }

    private fun showDeleteDialog() {
        val dialogTitle = getString(R.string.delete)
        val dialogMessage = getString(R.string.delete_msg)
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setPositiveButton("Ya") { _, _ ->
                val place = intent.getParcelableExtra<Place>(EXTRA_PLACE)
                if (place != null) {
                    dbHelper.delete(place)
                    showMessage("Photo has been deleted")
                }
                finish()
            }
            setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_PLACE = "extra_place"
    }

}