package com.example.xuedanzoustressmeter.ui.results

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

class ResultsFragment : Fragment() {

    private lateinit var resultsViewModel: ResultsViewModel
    private var _binding: FragmentResultsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var my_chart: LineChartView

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

        my_chart = view.findViewById(R.id.curve_chart)
        val isInterctive:Boolean= true
        my_chart.setInteractive(isInterctive)
        my_chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL)
        my_chart.setContainerScrollEnabled(true,  ContainerScrollType.HORIZONTAL)

        var axisX:Axis = Axis()
        axisX.setTextColor(Color.GRAY)
        axisX.setMaxLabelChars(5)
        axisX.setName("Instances")


        var axisY:Axis = Axis()
        axisY.setTextColor(Color.GRAY)
        axisY.setName("StressLevel")
        axisY.setHasLines(true)

        var values:MutableList<PointValue>? = ArrayList<PointValue>()
        values!!.add(PointValue(0f, 2f))
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

        var line: Line =  Line(values).setColor(Color.rgb(106,90,205))
                                      .setCubic(true)
                                      .setFilled(true)
        var lines: MutableList<Line> = ArrayList<Line>()
        lines.add(line)

        var data: LineChartData =  LineChartData()
        data.setLines(lines)

        data.axisYLeft = axisY
        data.axisXBottom = axisX

        my_chart.setLineChartData(data)

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}