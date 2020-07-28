package com.example.smstransactionreader.viewmodel

import android.content.Context
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smstransactionreader.repo.IncomeExpenseRepository
import com.example.smstransactionreader.model.IncomeExpense
import com.example.smstransactionreader.model.SmsInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class IncomeExpenseViewModel(private val context: Context) :ViewModel() {


    private val incomeExpenseLiveData: MutableLiveData<IncomeExpense> = MutableLiveData<IncomeExpense>()
    private val smsInfoListLiveData: MutableLiveData<List<SmsInfo>> = MutableLiveData<List<SmsInfo>>()

    @UiThread
    fun loadIncomeExpense(): LiveData<IncomeExpense>? {
        GlobalScope.launch(Dispatchers.Main) {
            val incomeExpense = async(Dispatchers.IO) {
                return@async IncomeExpenseRepository(
                    context
                ).fetchIncomeExpense()
            }.await()
            incomeExpenseLiveData.value = incomeExpense
        }
        return incomeExpenseLiveData
    }

    @UiThread
    fun searchMessage(tag: String): LiveData<List<SmsInfo>>? {
        GlobalScope.launch(Dispatchers.Main) {
            val smsInfoList = async(Dispatchers.IO) {
                return@async IncomeExpenseRepository(
                    context
                ).fetchAllSms(searchText = tag)
            }.await()
            smsInfoListLiveData.value = smsInfoList
        }
        return smsInfoListLiveData
    }
}