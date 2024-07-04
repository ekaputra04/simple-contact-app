package com.example.contactapp

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : Activity(), View.OnClickListener {
    private lateinit var btnInsert: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: Database
    private lateinit var cursor: Cursor

    companion object {
        const val REQUEST_CODE_INSERT = 1
        const val REQUEST_CODE_UPDATE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        btnInsert.setOnClickListener(this)
        database = Database(this)
        refreshList()
    }

    fun refreshList() {
        val db = database.readableDatabase
        val query = "SELECT * FROM kontak ORDER BY nama;"
        cursor = db.rawQuery(query, null)
        val daftar = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                daftar.add(cursor.getString(cursor.getColumnIndex("nama")))
            } while (cursor.moveToNext())
        }
        Log.i("daftarKontak", daftar.toString())
        recyclerView.adapter = KontakAdapter(this, daftar, database)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initComponents() {
        btnInsert = findViewById(R.id.btn_insert)
        recyclerView = findViewById(R.id.lv_kontak)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_insert) {
            val intent = Intent(this, CreateActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_INSERT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Tangani hasil dari UpdateActivity
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            refreshList()
        } else if (requestCode == REQUEST_CODE_INSERT && resultCode == Activity.RESULT_OK) {
            refreshList()
        }
    }
}
