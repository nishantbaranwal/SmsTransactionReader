package com.example.smstransactionreader.repo

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.smstransactionreader.model.IncomeExpense
import com.example.smstransactionreader.model.SmsInfo
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList

//Repository which gives the detail of total income and total expense
class IncomeExpenseRepository(private val context: Context) {

    companion object{
        private const val AUTHORITY = "sms/inbox"
        private const val PROVIDER_URI = "content://$AUTHORITY"
        private val CONTENT_URI = Uri.parse(PROVIDER_URI)
        private val TAG = this::class.simpleName
    }

    
     fun fetchIncomeExpense(): IncomeExpense? {
        var expense = 0f
        var income = 0f
        val cursor: Cursor? = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    var msgData = ""
                    for (idx in 0 until it.columnCount) {
                        msgData += " " + it.getColumnName(idx).toString() + ":" + it.getString(idx)
                        if (msgData.toLowerCase(Locale.ROOT).contains("debited")) {
                            var i = msgData.indexOf("Rs ")
                            if (i > 0) {
                                msgData = msgData.substring(i + 3)
                                msgData = msgData.substring(0, msgData.indexOf(" "))
                                try {
                                    if (msgData.contains(","))
                                        msgData = msgData.substring(
                                            0,
                                            msgData.indexOf(',')
                                        ) + msgData.substring(msgData.indexOf(',') + 1)
                                    expense += msgData.toFloat()
                                } catch (e: InputMismatchException) {
                                    Log.d(TAG, e.message?:"InputMismatchException")
                                } catch (e: NumberFormatException) {
                                    Log.d(TAG, e.message?:"NumberFormatException")
                                }
                            } else {
                                i = msgData.indexOf("INR ")
                                if (i > 0) {
                                    msgData = msgData.substring(i + 4)
                                    msgData = msgData.substring(0, msgData.indexOf(" "))
                                    try {
                                        if (msgData.contains(","))
                                            msgData = msgData.substring(
                                                0,
                                                msgData.indexOf(',')
                                            ) + msgData.substring(msgData.indexOf(',') + 1)
                                        expense += msgData.toFloat()
                                    } catch (e: InputMismatchException) {
                                        Log.d(TAG, e.message?:"InputMismatchException")
                                    } catch (e: NumberFormatException) {
                                        Log.d(TAG, e.message?:"NumberFormatException")
                                    }
                                }
                            }

                        }
                        if (msgData.toLowerCase(Locale.ROOT).contains("credited") ||
                            msgData.toLowerCase(Locale.ROOT).contains("deposited")
                        ) {
                            var i = msgData.indexOf("Rs ")
                            if (i > 0) {
                                msgData = msgData.substring(i + 3)
                                msgData = msgData.substring(0, msgData.indexOf(" "))
                                try {
                                    if (msgData.contains(","))
                                        msgData = msgData.substring(
                                            0,
                                            msgData.indexOf(',')
                                        ) + msgData.substring(msgData.indexOf(',') + 1)
                                    income += msgData.toFloat()
                                } catch (e: InputMismatchException) {
                                    Log.d(TAG, e.message?:"InputMismatchException")
                                } catch (e: NumberFormatException) {
                                    Log.d(TAG, e.message?:"NumberFormatException")
                                }
                            } else {
                                i = msgData.indexOf("INR ")
                                if (i > 0) {
                                    msgData = msgData.substring(i + 4)
                                    msgData = msgData.substring(0, msgData.indexOf(" "))
                                    try {
                                        if (msgData.contains(","))
                                            msgData = msgData.substring(
                                                0,
                                                msgData.indexOf(',')
                                            ) + msgData.substring(msgData.indexOf(',') + 1)
                                        income += msgData.toFloat()
                                    } catch (e: InputMismatchException) {
                                        Log.d(TAG, e.message?:"InputMismatchException")
                                    } catch (e: NumberFormatException) {
                                        Log.d(TAG, e.message?:"NumberFormatException")
                                    }
                                }
                            }
                        }
                    }
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        return IncomeExpense(
            income,
            expense
        )
    }


    fun fetchAllSms(searchText: String):List<SmsInfo> {
        val cursor: Cursor? = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        val smsInfoList = ArrayList<SmsInfo>()
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    var msgData = ""
                    val smsInfo =
                        SmsInfo()
                    for (idx in 0 until it.columnCount) {
                        msgData += " " + it.getColumnName(idx).toString() + ":" + it.getString(idx)
                        if (it.getColumnName(idx) != null && it.getString(idx)!=null) {
                            when (it.getColumnName(idx)) {
                                "person" -> smsInfo.person = it.getString(idx)
                                "body" -> smsInfo.body = it.getString(idx)
                                "date" -> smsInfo.date = it.getString(idx)
                                "address" -> smsInfo.person = it.getString(idx)
                            }
                            if (msgData.contains(searchText)) {
                                Log.d("asfsaf",msgData)
                                smsInfoList.add(smsInfo)
                            }
                        }
                    }

                }
                while (it.moveToNext())
            }
        }
        cursor?.close()
        return smsInfoList
    }

}