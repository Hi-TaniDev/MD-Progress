package com.example.hitani.jagung

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hitani.R
import com.example.hitani.data.DiagnosaJagung
import com.example.hitani.data.PlantReport
import com.example.hitani.view.PlantReportViewModel
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultJagungFragment : Fragment() {
    private lateinit var mPlantReportViewModel: PlantReportViewModel
    private lateinit var pathFile : String
    private lateinit var diagnosa : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result_jagung, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pathFile = arguments?.getString("imgPath").toString()
        println("gambar jagung : " + pathFile)
        diagnosa = arguments?.getString("diagnosa").toString()

        val takenPicture = BitmapFactory.decodeFile(pathFile)
        val imageView = view.findViewById<ImageView>(R.id.ivJagung)
        val labelPlant = view.findViewById<TextView>(R.id.tvLabel)
        val descPlant = view.findViewById<TextView>(R.id.tvDesc)
        val resJagung = rotatePicture(takenPicture)
        imageView?.setImageBitmap(resJagung)
        labelPlant.text = diagnosa
        descPlant.text = DiagnosaJagung().diagnosisMapJagung[diagnosa]
        mPlantReportViewModel = ViewModelProvider(this).get(PlantReportViewModel::class.java)
        view.findViewById<Button>(R.id.btnSave).setOnClickListener { addDiagnosaToDatabase()
            findNavController().navigate(R.id.action_rJagung_to_FirstFragment)
        }
        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().navigate(R.id.action_rJagung_to_FirstFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun addDiagnosaToDatabase() {
        val judul = view?.findViewById<EditText>(R.id.tvTitle)?.text.toString()
        val label = view?.findViewById<TextView>(R.id.tvLabel)?.text.toString()
        if (inputCheck(judul, pathFile, label)){
            val plantReport = PlantReport(0,judul, pathFile, label, LocalDateTime.now().format(
                DateTimeFormatter.ISO_DATE))
            mPlantReportViewModel.addLaporan(plantReport)
            Toast.makeText(requireContext(), "Laporan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Laporan gagal ditambahkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(label: String, imagePath: String, diagnosaReport: String): Boolean {
        return !(TextUtils.isEmpty(label) && TextUtils.isEmpty(imagePath) && TextUtils.isEmpty(diagnosaReport))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun rotatePicture(bitmap: Bitmap): Bitmap? {
        var eInterface: ExifInterface? = null
        try {
            eInterface = ExifInterface(pathFile)
        }catch (e: IOException) {
            e.printStackTrace()
        }
        val orientasi = eInterface!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val matx = Matrix()
        when(orientasi){
            ExifInterface.ORIENTATION_ROTATE_90 -> matx.setRotate(90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> matx.setRotate(180F)
            else ->{
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matx, true)
    }
}