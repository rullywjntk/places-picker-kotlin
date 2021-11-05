package com.rully.latihanapimaplocation.view.add

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rully.latihanapimaplocation.R
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ActivityAddBinding
import com.rully.latihanapimaplocation.helper.DatabaseHelper
import com.rully.latihanapimaplocation.view.detail.DetailActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class PlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityAddBinding? = null
    private val binding get() = _binding

    private var isEdit = false

    private val calendar = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var saveImage: Uri? = null
    private var place: Place? = null
    private lateinit var dbHelper: DatabaseHelper

    private var mLat: Double = 0.0
    private var mLon: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.P)
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val contentUri = data?.data
                val source =
                    contentUri?.let { it1 -> ImageDecoder.createSource(this.contentResolver, it1) }
                val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                if (bitmap != null) {
                    saveImage = saveImage(bitmap)
                    Log.e("Save image: ", "Path : $saveImage")
                }

                binding?.ivLocation?.let { image ->
                    Glide.with(applicationContext)
                        .load(data?.data)
                        .centerCrop()
                        .into(image)
                }
            }
        }

    val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    try {
                        val bitmap = data?.extras?.get("data") as Bitmap
                        Log.d("MyLogTag", "photo: $bitmap")
                        saveImage = saveImage(bitmap)
                        Log.e("Save image: ", "Path : $saveImage")
                        binding?.ivLocation?.let {
                            Glide.with(applicationContext)
                                .load(bitmap)
                                .into(it)
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.title = "Add Places"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        dbHelper = DatabaseHelper(this)

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            showDate()
        }
        showDate()

        binding?.etDate?.setOnClickListener(this)
        binding?.addImage?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.etDate -> {
                DatePickerDialog(
                    this@PlaceActivity,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.addImage -> {
                val imageDialog = AlertDialog.Builder(this)
                imageDialog.setTitle("Select Action")
                val imageDialogItems =
                    arrayOf("Select photo from Gallery", "Capture photo from camera")
                imageDialog.setItems(imageDialogItems) { _, which ->
                    when (which) {
                        0 -> selectPhotoFromGallery()
                        1 -> selectPhotoFromCamera()
                    }
                }.show()
            }
            R.id.btnSave -> {
                val title = binding?.etTitle?.text.toString().trim()
                val desc = binding?.etDesc?.text.toString().trim()
                val location = binding?.tvLocation?.text.toString().trim()
                val date = binding?.etDate?.text.toString().trim()
                when {
                    title.isEmpty() -> {
                        showMessage("Field is required")
                    }
                    location.isEmpty() -> {
                        showMessage("Field is required")
                    }
                    saveImage == null -> {
                        showMessage("Please select image")
                    }
                    else -> {
                        place = Place(0,title,saveImage.toString(),desc,date,location,mLat,mLon)
                        dbHelper.insert(place!!)
//                        place.let { place ->
//                            place?.title = title
////                            place?.image = saveImage?.toString()
////                            place?.description = desc
////                            place?.location = location
////                            place?.date = date
////                            place?.latitude = mLat
////                            place?.longitude = mLon
//                        }
//                        dbHelper.insert(place as Place)
                        showMessage("Added place successfully")
                        finish()
                    }
                }
            }
        }
    }


    private fun selectPhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    resultLauncher.launch(galleryIntent)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogPermissions()
            }
        }).onSameThread().check()
    }

    private fun selectPhotoFromCamera() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true) {
                    val cameraIntent =
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogPermissions()
            }
        }).onSameThread().check()
    }


    private fun showRationalDialogPermissions() {
        AlertDialog.Builder(this)
            .setMessage("You have turned off permission required for this feature")
            .setPositiveButton("Go to settings") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDate() {
        val formatDate = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(formatDate, Locale.getDefault())
        binding?.etDate?.setText(sdf.format(calendar.time).toString())
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveImage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(SAVE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val SAVE_DIRECTORY = "save_image"
    }
}