package com.example.contactapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DetailActivity : Activity(), View.OnClickListener {
    private lateinit var tvNama: TextView
    private lateinit var tvTelepon: TextView
    private lateinit var btnBack: ImageView
    private lateinit var database: Database
    private lateinit var imgUpdate: ImageView
    private lateinit var imgDelete: ImageView
    private lateinit var nama: String
    private lateinit var telepon: String

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
            nama = cursor.getString(cursor.getColumnIndex("nama"))
            telepon = cursor.getString(cursor.getColumnIndex("no_telepon"))
            // Tampilkan data pada TextView
            tvNama.text = nama
            tvTelepon.text = telepon
        }

        cursor.close()
        btnBack.setOnClickListener(this)
        imgUpdate.setOnClickListener(this)
        imgDelete.setOnClickListener(this)
    }

    private fun initComponents() {
        tvNama = findViewById(R.id.tv_detail_nama)
        btnBack = findViewById(R.id.btn_detail_back)
        tvTelepon = findViewById(R.id.tv_detail_telephone)
        database = Database(this)
        imgUpdate = findViewById(R.id.img_detail_edit)
        imgDelete = findViewById(R.id.img_detail_delete)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_detail_back -> finish()

            R.id.img_detail_edit -> {
                val intent = Intent(this, UpdateActivity::class.java)
                intent.putExtra(UpdateActivity.EXTRA_NAME, nama)
                startActivityForResult(intent, MainActivity.REQUEST_CODE_UPDATE)
                finish()
            }

            R.id.img_detail_delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Yakin menghapus kontak?")
                builder.setPositiveButton("Iya") { _, _ ->
                    val db: SQLiteDatabase = database.writableDatabase
                    db.delete("kontak", "nama = ?", arrayOf(nama))
                    Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show()
                    finish()
                }
                builder.setNegativeButton("Tidak", null)
                builder.create().show()
            }
        }
    }
}
