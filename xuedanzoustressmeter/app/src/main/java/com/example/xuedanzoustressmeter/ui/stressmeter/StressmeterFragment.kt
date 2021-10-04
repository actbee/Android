package com.example.xuedanzoustressmeter.ui.stressmeter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.xuedanzoustressmeter.ImageActivity
import com.example.xuedanzoustressmeter.MainActivity
import com.example.xuedanzoustressmeter.R
import com.example.xuedanzoustressmeter.databinding.FragmentStressmeterBinding
import android.content.SharedPreferences
import android.content.Context

class StressmeterFragment : Fragment() {

    private lateinit var stressmeterViewModel: StressmeterModel
    private var _binding: FragmentStressmeterBinding? = null
    // to control the shown image group
    private var state = 1
    private val binding get() = _binding!!

    // save the images
    private var grid_image_1 = intArrayOf(
        R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4,
        R.drawable.i5, R.drawable.i6, R.drawable.i7, R.drawable.i8,
        R.drawable.i9, R.drawable.i10, R.drawable.i11, R.drawable.i12,
        R.drawable.i13, R.drawable.i14, R.drawable.i15, R.drawable.i16
    )

    private var grid_image_2 = intArrayOf(
        R.drawable.i17, R.drawable.i18, R.drawable.i19, R.drawable.i20,
        R.drawable.i21, R.drawable.i22, R.drawable.i23, R.drawable.i24,
        R.drawable.i25, R.drawable.i26, R.drawable.i27, R.drawable.i28,
        R.drawable.i29, R.drawable.i30, R.drawable.i31, R.drawable.i32
    )

    private var grid_image_3 = intArrayOf(
        R.drawable.i33, R.drawable.i34, R.drawable.i35, R.drawable.i36,
        R.drawable.i37, R.drawable.i38, R.drawable.i39, R.drawable.i40,
        R.drawable.i41, R.drawable.i42, R.drawable.i43, R.drawable.i44,
        R.drawable.i45, R.drawable.i46, R.drawable.i47, R.drawable.i48
    )

    private var grid_value_1 = intArrayOf(
        1,10,3,8,
        7,9,11,12,
        2,5,6,4,
        10,13,16,15
    )

    private var grid_value_2 = intArrayOf(
        12,1,3,11,
        7,9,8,10,
        2,16,6,4,
        14,13,5,15
    )

    private var grid_value_3 = intArrayOf(
        15,7,3,8,
        16,5,2,10,
        14,13,6,4,
        11,1,9,12
    )

    lateinit var mygridview: GridView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        /*

       // not use viewmodel here
        stressmeterViewModel =
            ViewModelProvider(this).get(StressmeterModel::class.java)

        _binding = FragmentStressmeterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textStressmeter
        stressmeterViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        val root: View= binding.root
        return root

         */

        val pref: SharedPreferences = requireActivity().getSharedPreferences("stress_saved",Context.MODE_PRIVATE)
        state = pref.getInt("saved_state", (1..3).random())


        var view: View = inflater.inflate(R.layout.fragment_stressmeter, container, false)

        mygridview = view.findViewById(R.id.grid_images)

        val grid_adapter = GridAdapter(requireContext(), grid_image_1)
        val grid_adapter2 = GridAdapter(requireContext(), grid_image_2)
        val grid_adapter3 = GridAdapter(requireContext(), grid_image_3)

        when(state){
            1 -> mygridview.adapter = grid_adapter
            2 -> mygridview.adapter = grid_adapter2
            3 -> mygridview.adapter = grid_adapter3
        }
        mygridview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            var intent = Intent(requireContext(), ImageActivity::class.java)
            var input_image: Int = 0
            var input_value: Int = -1
            when(state){
                1 -> {
                    input_image = grid_image_1[position]
                    input_value = grid_value_1[position]
                }
                2 -> {
                    input_image = grid_image_2[position]
                    input_value = grid_value_2[position]
                }
                3 ->{
                    input_image = grid_image_3[position]
                    input_value = grid_value_3[position]
                }
            }
            intent.putExtra("image_id", input_image)
            intent.putExtra("value", input_value)
            pref.edit().putInt("saved_state", state).apply()
            startActivity(intent)
       //    requireActivity().finish()
        }

        val more_button = view.findViewById<Button>(R.id.more_images)!!
        more_button.setOnClickListener{ _ ->
            state += 1
            if(state == 4){
                state = 1
            }
            when(state){
                1 -> mygridview.adapter = grid_adapter
                2 -> mygridview.adapter = grid_adapter2
                3 -> mygridview.adapter = grid_adapter3
            }
            mygridview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                var intent = Intent(requireContext(), ImageActivity::class.java)
                var input_image: Int = 0
                var input_value: Int = -1
                when(state){
                    1 -> {
                        input_image = grid_image_1[position]
                        input_value = grid_value_1[position]
                    }
                    2 -> {
                        input_image = grid_image_2[position]
                        input_value = grid_value_2[position]
                    }
                    3 ->{
                        input_image = grid_image_3[position]
                        input_value = grid_value_3[position]
                    }
                }
                intent.putExtra("image_id", input_image)
                intent.putExtra("value", input_value)
                pref.edit().putInt("saved_state", state).apply()
                startActivity(intent)
             //  requireActivity().finish()
            }
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}