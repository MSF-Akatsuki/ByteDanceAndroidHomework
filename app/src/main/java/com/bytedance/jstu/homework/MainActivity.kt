package com.bytedance.jstu.homework

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.gson.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    private var btnTranslate : Button? = null
    private var txtInputTranslate : TextInputEditText? = null
    private var txtDispTranslate : TextInputEditText? = null
    private var tvResult : MaterialTextView? = null

    private val translationCache = HashMap<HttpUrl,TranslationCacheItem>()

    private val cacheQueue : LinkedList<Pair<HttpUrl, Int>> = LinkedList<Pair<HttpUrl, Int>>()
    //, dicts : List<List<String>>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTranslate = findViewById<Button>(R.id.buttonTranslate)
        txtInputTranslate = findViewById<TextInputEditText>(R.id.textInputTranslate)
        txtDispTranslate = findViewById<TextInputEditText>(R.id.textDispTranslate)
        tvResult = findViewById<MaterialTextView>(R.id.tvResult)


        val httpClient = OkHttpClient()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val gson = GsonBuilder().create()

        /* Seems to be a bug that defining this in xml doesn't work */
        // txtDispTranslate.inputType = InputType.TYPE_NULL

        btnTranslate?.setOnClickListener {
            btnTranslate?.isClickable = false
            Log.println(Log.INFO,"DispStr", txtInputTranslate?.text.toString())
            val rootUrl : String = "https://dict.youdao.com/jsonapi"

            /* Http Launching */
            val url = rootUrl.toHttpUrl().newBuilder()
                .addQueryParameter("q", txtInputTranslate?.text.toString())
                .build()
            if (!translationCache.containsKey(url)) {
                val request: Request = Request.Builder().url(url).build()
                httpClient.newCall(request).enqueue(callback)
            } else {
                Log.println(Log.INFO,"DispStr", "Use Cache")
                val tciTmp = translationCache[url]
                if (tciTmp!=null) {
                    applyTCI(tciTmp)
                } else {
                    Log.println(Log.INFO,"DispStr", "Null Cache Item!")
                }
            }

            btnTranslate?.isClickable = true
        }
    }

    val callback = object :Callback {

        override fun onFailure(call: Call, e: IOException) {
            runOnUiThread {
                txtDispTranslate?.text?.clear()
                txtDispTranslate?.text?.append("HttpFailure")
                Log.println(Log.WARN,"httperror",e.toString())
            }
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

            if (dicts.any{ it.asString == "fanyi" }) {
                val translationObject  = jsonObject.get("fanyi").asJsonObject
                strResult = translationObject.get("tran").asString
            } else {
                strResult = "None"
            }

            val tmpItem = TranslationCacheItem(
                call.request().url,strResult,guessLanguage
            )
            applyTCI(tmpItem)
            translationCache[call.request().url] = tmpItem

            /* Cache processing */
            val currentMillis = TimeZone.getDefault().getOffset(System.currentTimeMillis())
            cacheQueue.addLast(Pair(call.request().url,currentMillis))
            while (cacheQueue.isNotEmpty() && currentMillis - cacheQueue.first.second > 1000 * 600) {
                cacheQueue.removeFirst()
            }
        }
    }


    class TranslationCacheItem (url: HttpUrl, trans: String?, guessedLang: String) {
        val url = url
        val trans = trans
        val guessLang = guessedLang
        //val dicts = dicts
    }
    fun applyTCI(tci: TranslationCacheItem) {
        val hintLang : String
        if (tci.guessLang != "zh" && tci.guessLang != "eng") {
            Log.println(Log.INFO,"dispStr", "The language seems not supported. Plz input zh or en instead of ${tci.guessLang}")
            hintLang = "Is it in language ${tci.guessLang}? But not supported"
        } else {
            hintLang = "Seems fine!"
        }
        val strResult : String
        if (tci.trans != null) {
            strResult = tci.trans
        } else {
            strResult = "None"
        }

        runOnUiThread {
            if(txtDispTranslate!=null) {
                txtDispTranslate?.text?.clear()
                txtDispTranslate?.text?.append(strResult)
                tvResult?.text = hintLang
            }
        }

    }
}