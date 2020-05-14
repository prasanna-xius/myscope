package com.example.myscope.activities.prescription

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import com.example.myscope.R
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_pdfopenfile.*
import kotlinx.android.synthetic.main.custom_dialog.*

@ExperimentalStdlibApi
class PDFopenfile : AppCompatActivity() {
    lateinit var sharedpreferences:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfopenfile)
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        var format = sharedpreferences.getString("buffer",null)
//        format.encodeToByteArray()

//        val abc = format!!.encodeToByteArray()
//        var pureBase64Encoded = format!!.substring(format.indexOf(",") + 1);

        var decodedBytes = Base64.decode(format, Base64.DEFAULT)

//
//
//        val s: String = String(abc);
////        var uri: Uri = Uri.parse(s)
//        var uri1: Uri = Uri.parse(s)
//        pdf.fromUri(uri1).load()
      pdf.fromBytes(decodedBytes).load()
        pdfclose.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,Prescription_AddImage_PDF::class.java)
            startActivity(intent)
        })
    }
}
