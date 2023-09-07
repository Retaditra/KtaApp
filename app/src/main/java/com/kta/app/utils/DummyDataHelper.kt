package com.kta.app.utils

import android.content.Context
import com.kta.app.R
import org.json.JSONArray
import java.nio.charset.StandardCharsets

object DummyDataHelper {
    fun loadDummyData(context: Context): JSONArray {
        var dummyDataArray = JSONArray()
        val resources = context.resources
        try {
            // Membaca file JSON dari res/raw/dummy_data.json
            val inputStream = resources.openRawResource(R.raw.schedule_data)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()

            // Mengonversi byte array ke string
            val jsonString = String(buffer, StandardCharsets.UTF_8)

            // Mengonversi string menjadi objek JSON
            dummyDataArray = JSONArray(jsonString)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dummyDataArray
    }
}
