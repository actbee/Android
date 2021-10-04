package com.example.xuedanzoustressmeter.ui.stressmeter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.xuedanzoustressmeter.ImageActivity
import com.example.xuedanzoustressmeter.MainActivity
import com.example.xuedanzoustressmeter.R
import com.example.xuedanzoustressmeter.databinding.FragmentStressmeterBinding

class StressmeterFragment : Fragment() {

    private lateinit var stressmeterViewModel: StressmeterModel
    private var _binding: FragmentStressmeterBinding? = null

    private val binding get() = _binding!!

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

        var view: View = inflater.inflate(R.layout.fragment_stressmeter, container, false)

        val more_button = view.findViewById<Button>(R.id.more_images)!!
        more_button.setOnClickListener{ _ ->
            startActivity(Intent(requireContext(), ImageActivity::class.java))
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}