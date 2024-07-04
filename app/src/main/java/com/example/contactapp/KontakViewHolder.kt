package com.example.contactapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KontakViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvNamaKontak: TextView = itemView.findViewById(R.id.tv_nama_kontak)
}
