package com.example.contactapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class MainActivity : Activity(), View.OnClickListener {
    private lateinit var btnInsert: Button
    private lateinit var btnRefresh: Button
    private lateinit var listView: ListView
    private lateinit var database: Database
    private lateinit var cursor: Cursor

    companion object {
        private const val REQUEST_CODE_INSERT = 1
        private const val REQUEST_CODE_UPDATE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        btnInsert.setOnClickListener(this)
        btnRefresh.setOnClickListener(this)
        database = Database(this)
        refreshList()
    }

    @SuppressLint("Range")
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
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar)

        listView.setOnItemClickListener { parent, view, position, id ->
            val selection = daftar[position]
            val dialogItems = arrayOf("Lihat Kontak", "Update Kontak", "Hapus Kontak")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilihan")
            builder.setItems(dialogItems) { dialog, item ->
                when (item) {
                    0 -> {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_NAME, selection)
                        startActivity(intent)
                    }

                    1 -> {
//                        val intent = Intent(this@MainActivity, UpdateActivity::class.java)
//                        intent.putExtra(UpdateActivity.EXTRA_NAME, selection)
//                        startActivity(intent)
                        val intent = Intent(this, UpdateActivity::class.java)
                        intent.putExtra(UpdateActivity.EXTRA_NAME, selection)
                        startActivityForResult(intent, REQUEST_CODE_UPDATE)
                    }

                    2 -> {
                        val db: SQLiteDatabase = database.writableDatabase
                        db.execSQL("DELETE FROM kontak WHERE nama = '$selection';")
                        Toast.makeText(this@MainActivity, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show()
                        refreshList()
                    }
                }
            }
            builder.create().show()
        }
    }

    private fun initComponents() {
        btnInsert = findViewById(R.id.btn_insert)
        btnRefresh = findViewById(R.id.btn_refresh)
        listView = findViewById(R.id.lv_kontak)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_insert) {
            val intent = Intent(this, CreateActivity::class.java)
//            startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE_INSERT)
        } else if (v?.id == R.id.btn_refresh) {
            refreshList()
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
