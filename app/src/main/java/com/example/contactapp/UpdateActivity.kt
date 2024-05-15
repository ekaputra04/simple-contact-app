package com.example.contactapp

import android.annotation.SuppressLint
import android.app.Activity
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdateActivity : Activity(), View.OnClickListener {
    private lateinit var edtNama: EditText
    private lateinit var edtTelepon: EditText
    private lateinit var btnSimpan: Button
    private lateinit var database: Database
    private lateinit var nama: String
    private lateinit var name: String
    private lateinit var no_telepon: String

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        initComponents()

        val db: SQLiteDatabase = database.readableDatabase
        name = intent.getStringExtra(DetailActivity.EXTRA_NAME).toString()
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
            no_telepon = cursor.getString(cursor.getColumnIndex("no_telepon"))
            Log.i("namaInfo", nama)
            Log.i("namaInfo", no_telepon)
            edtNama.setText(cursor.getString(1).toString())
            edtTelepon.setText(cursor.getString(2).toString())
        }

        cursor.close()
        btnSimpan.setOnClickListener(this)
    }

    private fun initComponents() {
        edtNama = findViewById(R.id.edt_update_nama)
        edtTelepon = findViewById(R.id.edt_update_telepon)
        btnSimpan = findViewById(R.id.btn_update_simpan)
        database = Database(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btn_update_simpan) {
            val db: SQLiteDatabase = database.writableDatabase
            db.execSQL(
                "UPDATE kontak SET nama = '" +
                        edtNama.text.toString() + "', no_telepon = '" +
                        edtTelepon.text.toString() + "' WHERE nama = '" +
                        name + "';"
            )
            Toast.makeText(this, "Data Berhasil diupdate", Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_OK) // Set hasil dengan RESULT_OK
            finish() // Tutup aktivitas
        }
    }
}