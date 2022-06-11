package com.example.hitani.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hitani.R
import com.example.hitani.databinding.FragmentSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setVieAction()
        return root
    }

    private fun setVieAction() {
        binding.viewKeluar.setOnClickListener{

            dismiss()
        }
    }
}