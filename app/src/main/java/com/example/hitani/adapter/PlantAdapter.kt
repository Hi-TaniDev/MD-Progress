package com.example.hitani.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.exifinterface.media.ExifInterface
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.example.hitani.R
import com.example.hitani.data.PlantReport
import com.example.hitani.view.PlantReportActivityDirections
import java.io.IOException

class PlantAdapter: RecyclerView.Adapter<PlantAdapter.MyViewHolder>() {

    private var plantList = emptyList<PlantReport>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var nameReport: TextView = itemView.findViewById(R.id.plantTitle)
        private var tvDate: TextView = itemView.findViewById(R.id.plantDate)
        private var tvName: TextView = itemView.findViewById(R.id.plantName)
        private var ivPadi: ImageView = itemView.findViewById(R.id.ivPlant)
        private lateinit var imgPath: String
        private lateinit var laporan: PlantReport

        @RequiresApi(Build.VERSION_CODES.Q)
        fun bindData(data: PlantReport){
            nameReport.text = data.name
            tvDate.text = data.dateCreated
            tvName.text = data.laporanDiagnosis

            val takenPicture = BitmapFactory.decodeFile(data.imagePath)
            if (takenPicture != null){
                imgPath = data.imagePath
                println("Gambar Penyakit: "+ data.imagePath)
                val finalPicture = rotatePicture(takenPicture)
                ivPadi.setImageBitmap(finalPicture)
            }
            laporan = data
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun rotatePicture(bitmap: Bitmap): Bitmap? {
            var eInterface: ExifInterface? = null
            try {
                eInterface = ExifInterface(imgPath)
            } catch (e: IOException){
                e.printStackTrace()
            }
            val orientasi = eInterface!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            val matx = Matrix()
            when(orientasi){
                ExifInterface.ORIENTATION_ROTATE_90 -> matx.setRotate(90F)
                ExifInterface.ORIENTATION_ROTATE_180 -> matx.setRotate(180F)
                else->{
                }
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width-1, bitmap.height-1, matx, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_plant, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val nowReport = plantList.elementAt(position)
        holder.bindData(nowReport)
        holder.itemView.findViewById<ConstraintLayout>(R.id.rowPlant).setOnClickListener {
            val aksi = PlantReportActivityDirections.actionFirstFragmentToDetailPadi(nowReport)
            holder.itemView.findNavController().navigate(aksi)
        }
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    fun setData(plantReport: List<PlantReport>){
        this.plantList = plantReport
        notifyDataSetChanged()
    }
}