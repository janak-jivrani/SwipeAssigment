package com.assignment.products.core

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

fun getPathFromInputStreamUri(context: Context, uri: Uri): String? {
    var filePath: String? = null
    uri.authority?.let {
        try {
            context.contentResolver.openInputStream(uri).use {
                var tempFileName = "temp_${System.currentTimeMillis()}.png"

                val photoFile: File? = createTemporalFileFrom(it, tempFileName, context)
                filePath = photoFile?.path
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return filePath
}

@Throws(IOException::class)
private fun createTemporalFileFrom(
    inputStream: InputStream?, tempFileName: String, context: Context
): File? {
    var targetFile: File? = null
    return if (inputStream == null) targetFile
    else {
        var read: Int
        val buffer = ByteArray(8 * 1024)
        targetFile = createTemporalFile(context, tempFileName)
        FileOutputStream(targetFile).use { out ->
            while (inputStream.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            out.flush()
        }
        targetFile
    }
}

private fun createTemporalFile(context: Context, tempFileName: String): File {
    return File(context.cacheDir, tempFileName)
}