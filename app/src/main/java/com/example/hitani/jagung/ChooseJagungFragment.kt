package com.example.hitani.jagung

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.exifinterface.media.ExifInterface
import androidx.navigation.fragment.findNavController
import com.example.hitani.R
import java.io.File
import java.io.IOException
import java.io.InputStream

private const val PHOTO_REQUEST_CODE = 36
private const val READ_STORAGE_CODE = 39
private const val WRITE_STORAGE_CODE = 40
private const val IMAGE_PICK_CODE = 41
private const val FILE_NAME = "Photo"
private lateinit var photoFile: File
lateinit var imgBitmap: Bitmap
lateinit var rClassifier: CornClassifier

@Suppress("DEPRECATION")
class ChooseJagungFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rClassifier = CornClassifier("corn_model.tflite", "jagung_label.txt", requireContext())
        return inflater.inflate(R.layout.fragment_choose_jagung, container, false)
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnBatal).setOnClickListener {
            findNavController().navigate(R.id.action_ThirdFragment_to_FirstFragment)
            showButtons(view)
        }
        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            if (::photoFile.isInitialized){
                if (::imgBitmap.isInitialized){
                    val result = rClassifier.recognizeImg(imgBitmap)
                    val bundle = bundleOf("imgPath" to photoFile.toString(), "diagnosa" to result)

                    findNavController().navigate(R.id.action_ThirdFragment_to_rJagung, bundle)
                    showButtons(view)
                }
            }else{
                Toast.makeText(activity, "Gambar belum ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
        view.findViewById<Button>(R.id.btnCamera).setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getCamera(FILE_NAME)
            val fp = activity?.let { it ->
                FileProvider.getUriForFile(
                    it, "com.example.hitani.fileprovider", photoFile) }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fp)
            if (activity?.packageManager?.let {
                        it -> cameraIntent.resolveActivity(it)
                } != null){
                hideButton(view)
                startActivityForResult(cameraIntent, PHOTO_REQUEST_CODE)
            } else {
                Toast.makeText(activity, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }
        view.findViewById<Button>(R.id.btnUpload).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            photoFile = getCamera(FILE_NAME)
            println("image file: $photoFile")

            val fp = activity?.let { it -> FileProvider.getUriForFile(
                it, "com.example.hitani.fileprovider", photoFile
            ) }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fp)
            if (activity?.packageManager?.let { it ->
                    intent.resolveActivity(it)
                } != null){
                startActivityForResult(intent, IMAGE_PICK_CODE)
            }else{
                Toast.makeText(activity, "Gagal upload gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takePicture = BitmapFactory.decodeFile(photoFile.absolutePath)
            val imageView = view?.findViewById<ImageView>(R.id.ivJagung)
            val resImage = rotatePicture(takePicture)
            if (resImage != null){
                imgBitmap = resImage
                this.view?.let { hideButton(it) }
            }
            imageView?.setImageBitmap(resImage)
        }
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
            val choosePadiUri: Uri? = data!!.data
            var inputStream: InputStream
            if (choosePadiUri != null){
                inputStream = this.requireContext().contentResolver?.openInputStream(choosePadiUri)!!
                photoFile.copyInputStreamToFile(inputStream)
            }
            val takePicture = BitmapFactory.decodeFile(photoFile.absolutePath)
            val imageView = view?.findViewById<ImageView>(R.id.ivJagung)
            val resJagung = rotatePicture(takePicture)
            if (resJagung != null){
                imgBitmap = resJagung
                this.view?.let { hideButton(it) }}
            imageView?.setImageBitmap(resJagung)
        }
    }

    private fun File.copyInputStreamToFile(inputStream: InputStream){
        this.outputStream().use { fileOut -> inputStream.copyTo(fileOut) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun rotatePicture(bitmap: Bitmap): Bitmap? {
        var eInterface: ExifInterface? = null
        try {
            eInterface = ExifInterface(photoFile)
        }catch (e: IOException){
            e.printStackTrace()
        }
        val orientasi = eInterface!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
        )
        val matx = Matrix()
        when(orientasi){
            ExifInterface.ORIENTATION_ROTATE_90 -> matx.setRotate(90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> matx.setRotate(180F)
            else ->{
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matx, true)
    }

    private fun getCamera(fileName: String): File {
        val dir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(fileName, ".jpg", dir)
    }

    private fun hideButton(view: View) {
        view.findViewById<Button>(R.id.btnUpload).visibility = View.GONE
        view.findViewById<Button>(R.id.btnCamera).visibility = View.GONE
    }

    private fun showButtons(view: View) {
        view.findViewById<Button>(R.id.btnUpload).visibility = View.VISIBLE
        view.findViewById<Button>(R.id.btnCamera).visibility = View.VISIBLE
    }
}
