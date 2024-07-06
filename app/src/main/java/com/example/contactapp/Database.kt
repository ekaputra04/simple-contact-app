package com.example.contactapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Database(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "kontak.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Buat tabel dan aturan-aturan lainnya saat database pertama kali dibuat
        // Misalnya:
        // db?.execSQL("CREATE TABLE IF NOT EXISTS nama_tabel (...);")

        val sql = "CREATE TABLE IF NOT EXISTS kontak (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama TEXT NOT NULL UNIQUE," +
                "no_telepon TEXT NOT NULL UNIQUE);"
        Log.d("data", "OnCreate: $sql")
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Di sini Anda dapat menulis kode untuk mengupgrade struktur database saat versi database berubah
        // Misalnya:
        // db?.execSQL("DROP TABLE IF EXISTS nama_tabel;")
        // onCreate(db)
    }
}
