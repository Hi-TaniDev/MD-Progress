package com.example.hitani.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hitani.R
import com.example.hitani.adapter.PlantAdapter
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
        view.findViewById<FloatingActionButton>(R.id.jagung).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_JagungFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting -> {
                view?.findNavController()?.navigate(R.id.action_nav_main_to_setting)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}