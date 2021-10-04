package com.example.xuedanzoustressmeter.ui.results

import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.xuedanzoustressmeter.R
import com.example.xuedanzoustressmeter.databinding.FragmentResultsBinding
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.Chart
import lecho.lib.hellocharts.view.LineChartView
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList

class ResultsFragment : Fragment() {

    private lateinit var resultsViewModel: ResultsViewModel
    private var _binding: FragmentResultsBinding? = null
    private val TIME = 0
    private val STRESS = 1

    // This property is only valid between onCreateView and
    // onDestroyView.
        private val binding get() = _binding!!
        private lateinit var my_chart: LineChartView
        private lateinit var my_table: TableLayout
        private  var time_array = ArrayList<String>()
        private  var stress_array = ArrayList<Float>()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        /*
        resultsViewModel =
            ViewModelProvider(this).get(ResultsViewModel::class.java)

        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textResults
        resultsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root*/

        var view: View = inflater.inflate(R.layout.fragment_results, container, false)
        val file = File(requireContext().getExternalFilesDir(null), "customer.csv")
        file.createNewFile()
        val reader = file.reader()
        val mylines = reader.readLines()
        for (line in mylines) {
            Log.d("data read", line)
            val tokens = line.split(",")

            if(tokens.size>0){
                time_array.add(tokens[TIME])
                stress_array.add(tokens[STRESS].toFloat())
            }
        }
        reader.close()
        my_chart = view.findViewById(R.id.curve_chart)
        val isInterctive:Boolean= true
        my_chart.setInteractive(isInterctive)
        my_chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL)
        my_chart.setContainerScrollEnabled(true,  ContainerScrollType.HORIZONTAL)

        val axisX:Axis = Axis()
        axisX.setTextColor(Color.GRAY)
        axisX.setMaxLabelChars(5)
        axisX.setName("Instances")


        val axisY:Axis = Axis()
        axisY.setTextColor(Color.GRAY)
        axisY.setName("StressLevel")
        axisY.setHasLines(true)


        var values:MutableList<PointValue>? = ArrayList<PointValue>()
   /*     values!!.add(PointValue(0f, 2f))
        values!!.add(PointValue(1f, 5f))
        values!!.add(PointValue(2f, 8f))
        values!!.add(PointValue(3f, 4f))
        values!!.add(PointValue(4f, 10f))
        values!!.add(PointValue(5f,13f))
        values!!.add(PointValue(6f, 2f))
        values!!.add(PointValue(7f, 3f))
        values!!.add(PointValue(8f, 6f))
        values!!.add(PointValue(9f, 11f))
        values!!.add(PointValue(10f,10f))

    */
        // get the data of stress value
        var count_stress: Int = 0
        while(count_stress < stress_array.size){
            values!!.add(PointValue(count_stress.toFloat(),stress_array[count_stress]))
            count_stress++
        }

        val line: Line =  Line(values).setColor(Color.rgb(106,90,205))
                                      .setCubic(true)
                                      .setFilled(true)
        val lines: MutableList<Line> = ArrayList<Line>()
        lines.add(line)

        val data: LineChartData =  LineChartData()
        data.setLines(lines)

        data.axisYLeft = axisY
        data.axisXBottom = axisX

        my_chart.setLineChartData(data)

        my_table = view.findViewById(R.id.result_table)
        var count_time = 0
        while(count_time < time_array.size){
            Log.d("time stamp", time_array[count_time])
            val row_added = TableRow(requireContext())
            val lp: TableRow.LayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            row_added.setLayoutParams(lp)
            val time_view = TextView(requireContext())
            lp.width=700
            time_view.layoutParams = lp

            time_view.setText("  "+time_array[count_time])

            time_view.setBackgroundResource(R.drawable.stroke_textview)
            row_added.addView(time_view)

            var stress_view:TextView = TextView(requireContext())
            stress_view.setText(" "+time_array[count_time])
            var stress_lp: TableRow.LayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            stress_lp.width=400
            stress_view.layoutParams = stress_lp
            stress_view.setBackgroundResource(R.drawable.stroke_textview)
            row_added.addView(stress_view)
            my_table.addView(row_added, count_time)

            count_time++
        }


        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}