package com.bytedance.jstu.homework

import android.icu.text.IDNA
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class MainActivity : AppCompatActivity() {
    //, dicts : List<List<String>>
    class JsonRequestBody (q : String) {
        val q = q
        //val dicts = dicts
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTranslate = findViewById<Button>(R.id.buttonTranslate)
        val txtInputTranslate = findViewById<TextInputEditText>(R.id.textInputTranslate)
        val txtDispTranslate = findViewById<TextInputEditText>(R.id.textDispTranslate)

        val httpClient = OkHttpClient()
        val JSON = "application/json; charset=utf-8".toMediaType()

        val gson = GsonBuilder().create()

        val callback = object :Callback {
            override fun onFailure(call: Call, e: IOException) {
                txtDispTranslate.text?.replace(0,txtDispTranslate.length(),e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()

                val responseStr = response.body?.string()
                Log.println(Log.INFO, "dispStr",responseStr!!)

                val jsonObject = JsonParser.parseString(responseStr).asJsonObject
                val strResult : String

                val metaObject = jsonObject.get("meta").asJsonObject
                val dicts = metaObject.get("dicts").asJsonArray
                val guessLanguage = metaObject.get("guessLanguage").asString

                if (guessLanguage != "zh" && guessLanguage != "en") {
                    Log.println(Log.INFO,"dispStr", "The language seems not supported. Plz input zh or en instead of $guessLanguage")
                }

                if (dicts.any{ it.asString == "fanyi" }) {
                    val translationObject  = jsonObject.get("fanyi").asJsonObject
                    strResult = translationObject.get("tran").asString
                } else {
                    strResult = "None"
                }

                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    txtDispTranslate.text?.replace(0,txtDispTranslate.length(),strResult)
                }

            }
        }

        /* Seems to be a bug that defining this in xml doesn't work */
        // txtDispTranslate.inputType = InputType.TYPE_NULL

        btnTranslate.setOnClickListener {
            Log.println(Log.INFO,"DispStr", txtInputTranslate.text.toString())
            val rootUrl : String = "https://dict.youdao.com/jsonapi"


            val url = rootUrl.toHttpUrl().newBuilder()
                .addQueryParameter("q", txtInputTranslate.text.toString())
                .build()

            val request : Request = Request.Builder().url(url).build()
            val response = httpClient.newCall(request).enqueue(callback)

        }
    }
}