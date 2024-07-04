package com.example.contactapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : Activity(), View.OnClickListener {
    private lateinit var btnInsert: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: Database
    private lateinit var cursor: Cursor
    private lateinit var edtSearch: EditText
    private lateinit var imgSearch: ImageView
    private lateinit var tvStatusKontak: TextView

    companion object {
        const val REQUEST_CODE_INSERT = 1
        const val REQUEST_CODE_UPDATE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        btnInsert.setOnClickListener(this)
        imgSearch.setOnClickListener(this)
        database = Database(this)

        refreshList("")
    }

    override fun onResume() {
        super.onResume()
        refreshList("")
    }

    @SuppressLint("Range")
    fun refreshList(kondisi: String) {
        val db = database.readableDatabase
        val query =
            "SELECT * FROM kontak WHERE nama LIKE '%$kondisi%' ORDER BY nama;" // perbaikan di sini
        cursor = db.rawQuery(query, null)
        val daftar = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                daftar.add(cursor.getString(cursor.getColumnIndex("nama")))
            } while (cursor.moveToNext())
        }

        if (daftar.isEmpty()) {
            tvStatusKontak.visibility = View.VISIBLE
        } else {
            tvStatusKontak.visibility = View.GONE
        }
        recyclerView.adapter = KontakAdapter(this, daftar, database)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initComponents() {
        btnInsert = findViewById(R.id.btn_insert)
        recyclerView = findViewById(R.id.lv_kontak)
        edtSearch = findViewById(R.id.edt_main_search)
        imgSearch = findViewById(R.id.img_main_search)
        tvStatusKontak = findViewById(R.id.tv_main_kontak_tidak_tersedia)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_insert) {
            val intent = Intent(this, CreateActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_INSERT)
        }

        if (v?.id == R.id.img_main_search) {
            val query = edtSearch.text.trim()
            refreshList(query.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Tangani hasil dari UpdateActivity
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            refreshList("")
        } else if (requestCode == REQUEST_CODE_INSERT && resultCode == Activity.RESULT_OK) {
            refreshList("")
        }
    }
}
