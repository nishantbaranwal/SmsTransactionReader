package com.example.smstransactionreader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permission =  arrayOf(Manifest.permission.SEND_SMS)

        if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permission, 1)
        }
        else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        }

        see_pie_chart_btn.setOnClickListener {
            val intent = Intent(this, IncomeExpenseActivity::class.java)
            intent.putExtra("type","pie chart")
            startActivity(intent)
        }
        all_transaction_btn.setOnClickListener {
            val intent = Intent(this, IncomeExpenseActivity::class.java)
            intent.putExtra("type","all transaction")
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Sms Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Sms Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        settings_btn.setOnClickListener {

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)

        }
    }


}