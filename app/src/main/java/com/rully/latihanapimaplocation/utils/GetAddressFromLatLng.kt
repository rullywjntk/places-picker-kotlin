package com.rully.latihanapimaplocation.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.util.*

class GetAddressFromLatLng(context: Context, private val lat: Double, private val lon: Double) : AsyncTask<Void, String, String>() {

    private val geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListener: AddressListener

    override fun doInBackground(vararg p0: Void?): String {
        try {
            val addressList: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
            if (addressList != null && addressList.isEmpty()) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(",")
                }
                sb.deleteCharAt(sb.length - 1)
                return sb.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Places", "Unable connect to geocoder")
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        if (result == null){
            mAddressListener.onError()
        } else {
            mAddressListener.onAddressFound(result)
        }
        super.onPostExecute(result)
    }

    fun setAddressListener(addressListener: AddressListener) {
        mAddressListener = addressListener
    }

    fun getAddress() {
        execute()
    }
    interface AddressListener {
        fun onAddressFound(address: String)
        fun onError()
    }

}