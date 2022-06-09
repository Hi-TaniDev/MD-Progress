package com.example.hitani.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hitani.R
import com.example.hitani.adapter.PlantAdapter
import com.example.hitani.databinding.ActivityPlantReportBinding
import com.getbase.floatingactionbutton.FloatingActionButton

class PlantReportActivity : Fragment(){

    private lateinit var mPlantReportViewModel: PlantReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_plant_report, container, false)
        val adapter = PlantAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvReport)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mPlantReportViewModel = ViewModelProvider(this).get(PlantReportViewModel::class.java)
        mPlantReportViewModel.readAllData.observe(viewLifecycleOwner, Observer { laporan ->  adapter.setData(laporan)})
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FloatingActionButton>(R.id.padi).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_PadiFragment)
        }
    }
}