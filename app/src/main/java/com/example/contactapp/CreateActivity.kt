package com.example.contactapp

import android.app.Activity
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CreateActivity : Activity(), View.OnClickListener {
    private lateinit var edtNama: EditText
    private lateinit var edtTelepon: EditText
    private lateinit var btnSimpan: Button
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initComponents()
        btnSimpan.setOnClickListener(this)
    }

    private fun initComponents() {
        edtNama = findViewById(R.id.edt_create_nama)
        edtTelepon = findViewById(R.id.edt_create_telepon)
        btnSimpan = findViewById(R.id.btn_create_simpan)
        database = Database(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_create_simpan) {
            val db: SQLiteDatabase = database.writableDatabase
            db.execSQL(
                "INSERT INTO kontak(nama, no_telepon) VALUES('" +
                        edtNama.text.toString() + "', '" +
                        edtTelepon.text.toString() + "');"
            )
            Toast.makeText(this@CreateActivity, "Data Berhasil Tersimpan", Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_OK) // Set hasil dengan RESULT_OK
            finish() // Tutup aktivitas
        }
    }
}
