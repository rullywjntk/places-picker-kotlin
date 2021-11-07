package com.rully.latihanapimaplocation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rully.latihanapimaplocation.R
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.databinding.ListPlaceBinding
import com.rully.latihanapimaplocation.helper.DatabaseHelper

class PlaceAdapter: RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private val listPlaces = ArrayList<Place>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(listPlace: List<Place>) {
        listPlaces.clear()
        listPlaces.addAll(listPlace)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val binding: ListPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(place)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(place.image)
                    .error(R.drawable.add_screen_image_placeholder)
                    .into(roundedImageItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPlaces[position])
    }

    override fun getItemCount(): Int = listPlaces.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Place)
    }
}