package com.example.smstransactionreader.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smstransactionreader.R
import com.example.smstransactionreader.model.SmsInfo
import java.util.*

class SmsReaderAdapter(private val list: List<SmsInfo>): RecyclerView.Adapter<SmsReaderAdapter.SmsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        return SmsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.sms_list_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val bodyTv = holder.itemView.findViewById<TextView>(R.id.body_tv)
        val dateTv = holder.itemView.findViewById<TextView>(R.id.date_tv)
        val personTv = holder.itemView.findViewById<TextView>(R.id.person_tv)
        bodyTv.text = list[position].body
        val date = Date(list[position].date.toLong())
        if(date.minutes<10)
            dateTv.text = "${date.hours} : 0${date.minutes}"
        else
            dateTv.text = "${date.hours} : ${date.minutes}"
        personTv.text = list[position].person
    }

    class SmsViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)
}