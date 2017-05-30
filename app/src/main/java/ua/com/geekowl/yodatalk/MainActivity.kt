package ua.com.geekowl.yodatalk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val LOG_TAG = "YODA TALK APP"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FuelManager.instance.basePath = "https://yoda.p.mashape.com/yoda?sentence="
        FuelManager.instance.baseHeaders = mapOf("X-Mashape-Key" to getString(R.string.YODA_SPEACH_TOKEN),
                "Accept" to "text/plain")
        sendButton.setOnClickListener {
            val userSentence = sentenceTextView.text
            if(userSentence.length < 5){

            }
            Fuel.get("$userSentence").responseString { request, response, result ->
                //do something with response
                result.fold({ serverData ->
                    //do something with data
                    Log.v(LOG_TAG, serverData.toString())
                }, { err ->
                    //do something with error
                    Log.e(LOG_TAG, err.message)
                })
            }
        }
    }
}
