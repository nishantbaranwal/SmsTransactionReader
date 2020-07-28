package com.example.smstransactionreader

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smstransactionreader.view.adapter.SmsReaderAdapter
import com.example.smstransactionreader.viewmodel.IncomeExpenseViewModel
import com.example.smstransactionreader.viewmodel.IncomeExpenseViewModelFactory
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_income_expense.*


class IncomeExpenseActivity : AppCompatActivity() {

    companion object{
        val MATERIAL_COLORS = intArrayOf(
            ColorTemplate.rgb("9C27B0"),
            ColorTemplate.rgb("#5365CC"),
            ColorTemplate.rgb("#e74c3c"),
            ColorTemplate.rgb("#3498db")
        )
    }

    private fun getViewModel(): IncomeExpenseViewModel {
        return ViewModelProvider(this,
            IncomeExpenseViewModelFactory(
                this
            )
        ).get(
            IncomeExpenseViewModel::class.java)
    }

    //legend is the top right coenwe
    private fun setLegend(expense: Float, income: Float) {
        val l: Legend = pie_chart.legend
        l.formSize = 10f // set the size of the legend forms/shapes
        l.form = Legend.LegendForm.SQUARE // set what type of form/shape should be used
        l.yOffset = 5f
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP // set vertical alignment for legend
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // set horizontal alignment for legend
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.textSize = 12f
        l.textColor = Color.BLACK
        l.yEntrySpace = 1f // set the space between the legend entries on the y-axis
        // set custom labels and colors
        val entries: MutableList<LegendEntry> = ArrayList()
        val yValues: ArrayList<PieEntry> = ArrayList()
        yValues.add(PieEntry(expense, "Expense"))
        yValues.add(PieEntry(income, "Income"))
        for (i in 0 until yValues.size) {
            val entry = LegendEntry()
            entry.formColor = MATERIAL_COLORS[i]
            entry.label = yValues[i].label
            entries.add(entry)
        }
        l.setCustom(entries)
        pie_chart.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_expense)

        val type = intent.getStringExtra("type")
        if(type == "pie chart") {
            progress_bar.visibility = View.VISIBLE
            loadPieChart()
        }
        else
            if(type == "all transaction") {
                search_view.visibility = View.VISIBLE
                search_view.setOnQueryTextListener(TextListener())
                search_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            }
    }

    inner class TextListener: androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            if (p0 != null) {
                progress_bar.visibility = View.VISIBLE
                getViewModel().searchMessage(p0)?.observe(this@IncomeExpenseActivity, Observer {
                    val adapter =
                        SmsReaderAdapter(
                            it
                        )
                    search_list.adapter = adapter
                    adapter.notifyDataSetChanged()
                    progress_bar.visibility = View.GONE
                    search_list.visibility = View.VISIBLE

                })
            }
            return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            return false
        }

    }

    private fun loadPieChart(){
        getViewModel().loadIncomeExpense()?.observe(this, Observer {
            pie_chart.description.isEnabled = true
            pie_chart.description = Description().apply {
                text = "Ratio B/W expense and income"
                textColor = Color.WHITE
                textSize = 12f
            }
            pie_chart.setExtraOffsets(5f, 5f, 10f, 5f)
            pie_chart.dragDecelerationFrictionCoef = 0.9f
            pie_chart.setDrawCenterText(false)
            pie_chart.isDrawHoleEnabled = false
            val yValues: ArrayList<PieEntry> = ArrayList()
            yValues.add(PieEntry(it.expense, "Expense"))
            yValues.add(PieEntry(it.income, "Income"))
            pie_chart.setDrawEntryLabels(true)
            val dataSet = PieDataSet(yValues, "Ratio B/w expense and income")
            dataSet.sliceSpace = 0.1f
            dataSet.setDrawValues(true)
            dataSet.selectionShift = 5f

            dataSet.colors = MATERIAL_COLORS.asList()
            val pieData = PieData(dataSet)
            pieData.setValueTextSize(10f)
            pieData.setValueTextColor(Color.BLACK)
            pie_chart.data = pieData
            pie_chart.setDrawEntryLabels(true)
            setLegend(it.expense, it.income)
            progress_bar.visibility = View.GONE

        })
    }
}