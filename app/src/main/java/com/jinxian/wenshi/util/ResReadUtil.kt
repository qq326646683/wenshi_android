package com.jinxian.wenshi.util

import android.content.res.Resources
import android.util.Log
import com.jinxian.wenshi.AppContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object ResReadUtil {
    fun readResource(resourceId: Int): String {
        val builder = StringBuilder()
        try {
            val inputStream = AppContext.resources.openRawResource(resourceId)
            val streamReader = InputStreamReader(inputStream)

            val bufferedReader = BufferedReader(streamReader)
            var textLine: String? = null
            while ((bufferedReader.readLine()?.also { textLine = it }) != null) {
                builder.append(textLine)
                builder.append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        return builder.toString()
    }
}