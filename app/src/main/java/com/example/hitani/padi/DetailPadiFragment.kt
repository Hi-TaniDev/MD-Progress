package com.example.hitani.padi

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hitani.R
import com.example.hitani.data.DiagnosaPlant
import com.example.hitani.view.PlantReportViewModel
import java.io.IOException

class DetailPadiFragment : Fragment(){
    private val args by navArgs<DetailPadiFragmentArgs>()
    private lateinit var mPlantReportViewModel: PlantReportViewModel
    private lateinit var pathFile: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_padi, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pathFile = args.nowReport.imagePath
        println("imgPath:  " + pathFile)
        val takenPicture = BitmapFactory.decodeFile(pathFile)
        if (takenPicture != null){
            val imageView = view.findViewById<ImageView>(R.id.detailImage)
            val resPadi = rotatePicture(takenPicture)
            imageView?.setImageBitmap(resPadi)
        }
        view.findViewById<TextView>(R.id.detailTitle).setText(args.nowReport.name)
        view.findViewById<TextView>(R.id.detailLabel).setText(args.nowReport.laporanDiagnosis)
        view.findViewById<TextView>(R.id.detailDesc).text = DiagnosaPlant().diagnosisMap.get(args.nowReport.laporanDiagnosis)
        mPlantReportViewModel = ViewModelProvider(this).get(PlantReportViewModel::class.java)
        view.findViewById<Button>(R.id.btnHapus).setOnClickListener {
            hapusData()
            findNavController().navigate(R.id.action_detailPadi_to_FirstFragment)
        }
        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().navigate(R.id.action_detailPadi_to_FirstFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun rotatePicture(bitmap: Bitmap): Bitmap? {
        var eInterface: ExifInterface? = null
        try {
            eInterface = ExifInterface(pathFile)
        } catch (e: IOException){
            e.printStackTrace()
        }
        val orientasi = eInterface!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val matx = Matrix()
        when(orientasi){
            ExifInterface.ORIENTATION_ROTATE_90 -> matx.setRotate(90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> matx.setRotate(180F)
            else ->{}
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matx, true)
    }

    private fun hapusData(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Hapus"){_, _ ->
            mPlantReportViewModel.deleteLaporan(args.nowReport)
        }
        builder.setNegativeButton("Batal"){_,_ ->}
        builder.setTitle("Hapus Laporan")
        builder.setMessage("Apakah anda yakin ingin menghapus laporan ini?")
        builder.create().show()
    }
}