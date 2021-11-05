package com.rully.latihanapimaplocation.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rully.latihanapimaplocation.R
import com.rully.latihanapimaplocation.view.add.PlaceActivity

class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.findViewById<TextView>(R.id.tvShare).setOnClickListener {
            Toast.makeText(context, "Share Coming Soon", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<TextView>(R.id.tvEdit).setOnClickListener {
            Toast.makeText(context, "Edit Coming Soon", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<TextView>(R.id.tvDelete).setOnClickListener {
            Toast.makeText(context, "Delete Coming Soon", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        const val TAG = "BottomSheetFragment"
    }
}