package com.nutrino.mlmodelinandroid.ml

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Audionoisemodel private constructor(modelBuffer: MappedByteBuffer) {

    private val interpreter = Interpreter(modelBuffer)

    companion object {
        fun newInstance(context: Context): Audionoisemodel {
            val modelBuffer = loadModelFile(context, "audionoisemodel.tflite")
            return Audionoisemodel(modelBuffer)
        }

        private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
            val fileDescriptor = context.assets.openFd(modelName)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }

    fun process(inputBuffer: ByteBuffer): Outputs {
        val outputSize = 1 * 128 * 512 * 1 * 4
        val outputBuffer = ByteBuffer.allocateDirect(outputSize)
        outputBuffer.order(ByteOrder.nativeOrder())
        
        interpreter.run(inputBuffer, outputBuffer)
        
        return Outputs(outputBuffer)
    }

    fun close() {
        interpreter.close()
    }

    inner class Outputs(val buffer: ByteBuffer) {
        val outputFeature0AsTensorBuffer: ByteBuffer
            get() = buffer
    }
}
