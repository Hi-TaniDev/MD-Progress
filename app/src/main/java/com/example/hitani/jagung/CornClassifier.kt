package com.example.hitani.jagung

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class CornClassifier {
    private val imageSize: Int = 100
    val tflite: Interpreter
    val labelList: List<String>
    private val imgMean = 0
    private val imgStd = 255.0f

    constructor(modelPath: String, labelPath: String, context: Context){
        tflite = Interpreter(loadModelFile(modelPath, context))
        labelList = context.assets.open(labelPath).bufferedReader().useLines { it.toList() }
    }

    private fun loadModelFile(modelPath: String, context: Context): MappedByteBuffer {
        val fileDesc = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDesc.fileDescriptor)
        val fileChannel = inputStream.channel
        val sos = fileDesc.startOffset
        val dl = fileDesc.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, sos, dl)
    }

    fun recognizeImg(bitmap: Bitmap): String {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)
        val result = Array(1){
            FloatArray(labelList.size)
        }
        tflite.run(convertBitmapToByteBuffer(scaledBitmap), result)

        var cnt = 0
        var maxCnt = 0
        var max = 0.0f
        for(i in result){
            for (j in i){
                cnt++
                if (j > max){
                    max = j
                    maxCnt = cnt
                }
            }
        }
        if (max > 0.4f){
            val labelPlant = labelList[maxCnt-1]
            val newValue = labelPlant.substring(labelPlant.indexOf(" ") + 1)
            return newValue
        } else return "Unknown"
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until imageSize){
            for (j in 0 until imageSize){
                val `val` = intValues[pixel++]
                byteBuffer.putFloat((((`val`.shr(16)  and 0xFF) - imgMean) / imgStd))
                byteBuffer.putFloat((((`val`.shr(8) and 0xFF) - imgMean) / imgStd))
                byteBuffer.putFloat((((`val` and 0xFF) - imgMean) / imgStd))
            }
        }
        return byteBuffer
    }
}