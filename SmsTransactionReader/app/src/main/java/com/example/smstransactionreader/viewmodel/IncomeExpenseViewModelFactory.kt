package com.example.smstransactionreader.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class IncomeExpenseViewModelFactory(private val context: Context):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(IncomeExpenseViewModel::class.java)){
            return IncomeExpenseViewModel(
                context
            ) as T
        }
        throw IllegalArgumentException("No such class for ViewModelException $modelClass")
    }

}