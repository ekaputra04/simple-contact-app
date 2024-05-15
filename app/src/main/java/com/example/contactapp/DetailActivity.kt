package com.example.contactapp

import android.annotation.SuppressLint
import android.app.Activity
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class DetailActivity : Activity(), View.OnClickListener {
    private lateinit var tvNama: TextView
    private lateinit var btnBack: Button
    private lateinit var tvTelepon: TextView
    private lateinit var database: Database

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initComponents()

        val db: SQLiteDatabase = database.readableDatabase
        val name = intent.getStringExtra(EXTRA_NAME)
        val selection = "nama = ?"
        val selectionArgs = arrayOf(name)
        val cursor: Cursor = db.query(
            "kontak", // Nama tabel
            null, // Kolom yang akan diambil (null untuk mengambil semua kolom)
            selection, // Klausa WHERE
            selectionArgs, // Argumen untuk klausa WHERE
            null, // Grup oleh
            null, // Having
            null // Order by
        )

        if (cursor.moveToFirst()) {
            // Lakukan sesuatu dengan data dari cursor
            val nama = cursor.getString(cursor.getColumnIndex("nama"))
            val telepon = cursor.getString(cursor.getColumnIndex("no_telepon"))
            // Tampilkan data pada TextView
            tvNama.text = nama
            tvTelepon.text = telepon
        }

        cursor.close()
        btnBack.setOnClickListener(this)
    }

    private fun initComponents() {
        tvNama = findViewById(R.id.tv_detail_nama)
        btnBack = findViewById(R.id.btn_detail_back)
        tvTelepon = findViewById(R.id.tv_detail_telepon)
        database = Database(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_detail_back) {
            finish()
        }
    }
}