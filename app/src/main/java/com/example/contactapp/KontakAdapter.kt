package com.example.contactapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class KontakAdapter(private val context: Context, private val kontakList: List<String>, private val database: Database) :
    RecyclerView.Adapter<KontakViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontakViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_kontak, parent, false)
        return KontakViewHolder(view)
    }

    override fun onBindViewHolder(holder: KontakViewHolder, position: Int) {
        holder.tvNamaKontak.text = kontakList[position]

        holder.itemView.setOnClickListener {
            val selection = kontakList[position]
            val dialogItems = arrayOf("Lihat Kontak", "Update Kontak", "Hapus Kontak")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilihan")
            builder.setItems(dialogItems) { dialog, item ->
                when (item) {
                    0 -> {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_NAME, selection)
                        context.startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(context, UpdateActivity::class.java)
                        intent.putExtra(UpdateActivity.EXTRA_NAME, selection)
                        (context as Activity).startActivityForResult(intent, MainActivity.REQUEST_CODE_UPDATE)
                    }
                    2 -> {
                        val db: SQLiteDatabase = database.writableDatabase
                        db.execSQL("DELETE FROM kontak WHERE nama = '$selection';")
                        Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show()
                        (context as MainActivity).refreshList()
                    }
                }
            }
            builder.create().show()
        }
    }

    override fun getItemCount(): Int {
        return kontakList.size
    }
}
